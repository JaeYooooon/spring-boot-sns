package com.zerobase.sns.domain.user.service;

import static com.zerobase.sns.domain.follow.entity.FollowStatus.FOLLOWING;
import static com.zerobase.sns.global.exception.ErrorCode.ALREADY_EXIST_NICKNAME;
import static com.zerobase.sns.global.exception.ErrorCode.ALREADY_EXIST_USER;
import static com.zerobase.sns.global.exception.ErrorCode.NONE_CORRECT_PW;
import static com.zerobase.sns.global.exception.ErrorCode.NOT_FOUND_USER;

import com.zerobase.sns.domain.alarm.entity.Alarm;
import com.zerobase.sns.domain.alarm.repository.AlarmRepository;
import com.zerobase.sns.domain.follow.dto.FollowerDTO;
import com.zerobase.sns.domain.follow.dto.FollowingDTO;
import com.zerobase.sns.domain.user.dto.UserDTO;
import com.zerobase.sns.domain.user.dto.UserFollowListDTO;
import com.zerobase.sns.domain.user.dto.UserReqDTO;
import com.zerobase.sns.domain.user.dto.UserUpdateDTO;
import com.zerobase.sns.domain.user.entity.User;
import com.zerobase.sns.domain.user.repository.UserRepository;
import com.zerobase.sns.global.exception.CustomException;
import com.zerobase.sns.global.exception.ErrorCode;
import com.zerobase.sns.global.redis.RedisLockRepository;
import com.zerobase.sns.global.security.JwtTokenProvider;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;
  private final RedisLockRepository redisLockRepository;
  private final AlarmRepository alarmRepository;

  // 회원 가입
  @Transactional
  public User join(UserDTO joinDTO) throws InterruptedException {
    validateUserId(joinDTO.getUserId());
    validateUserPw(joinDTO.getPassword());

    String userId = joinDTO.getUserId();
    String nickname = joinDTO.getNickName();

    if (userRepository.existsByUserId(userId)) {
      throw new CustomException(ALREADY_EXIST_USER);
    }

    if (userRepository.existsByNickName(nickname)) {
      throw new CustomException(ALREADY_EXIST_NICKNAME);
    }

    while (!redisLockRepository.lock(userId)) {
      Thread.sleep(100);
    }
    while (!redisLockRepository.lock(nickname)) {
      Thread.sleep(100);
    }

    try {
      User user = User.builder()
          .userId(userId)
          .password(passwordEncoder.encode(joinDTO.getPassword()))
          .nickName(nickname)
          .isPrivate(joinDTO.getIsPrivate())
          .build();

      userRepository.save(user);
      return user;
    } finally {
      redisLockRepository.unlock(userId);
      redisLockRepository.unlock(nickname);
    }
  }

  // 로그인
  @Transactional
  public String login(UserReqDTO loginDTO) {
    User user = userRepository.findByUserId(loginDTO.getUserId())
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

    if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
      throw new CustomException(NONE_CORRECT_PW);
    }

    return jwtTokenProvider.createToken(user.getUserId());
  }

  // 회원 탈퇴
  @Transactional
  public void deleteUser(Principal principal) {
    User user = getUserByPrincipal(principal);

    userRepository.delete(user);
  }

  // 회원정보 수정
  public User updateUser(UserUpdateDTO userUpdateDTO, Principal principal)
      throws InterruptedException {
    validateUserPw(userUpdateDTO.getPassword());

    User user = getUserByPrincipal(principal);

    String newPassword = Optional.ofNullable(userUpdateDTO.getPassword())
        .orElse(user.getPassword());
    String newNickName = Optional.ofNullable(userUpdateDTO.getNickName())
        .orElse(user.getNickName());

    if (newNickName != null && !newNickName.equals(user.getNickName())) {
      while (!redisLockRepository.lock(newNickName)) {
        Thread.sleep(100);
      }
      try {
        if (userRepository.existsByNickName(newNickName)) {
          throw new CustomException(ALREADY_EXIST_NICKNAME);
        }

        user.setNickName(newNickName);
      } finally {
        redisLockRepository.unlock(newNickName);
      }
    }

    user.setNickName(user.getNickName());
    user.setPassword(passwordEncoder.encode(newPassword));

    userRepository.save(user);

    return user;
  }


  // 공개 범위 설정
  @Transactional
  public User updatePrivacy(boolean isPrivate, Principal principal) {
    User user = getUserByPrincipal(principal);

    user.setIsPrivate(isPrivate);
    userRepository.save(user);

    return user;
  }

  // 본인 정보 조회
  public UserDTO getUserDetail(Principal principal) {
    User user = getUserByPrincipal(principal);

    return UserDTO.builder()
        .userId(user.getUserId())
        .password(user.getPassword())
        .nickName(user.getNickName())
        .isPrivate(user.getIsPrivate())
        .build();
  }

  // 본인 팔로우/팔로잉 목록 조회
  public UserFollowListDTO getFollowList(Principal principal) {
    User user = getUserByPrincipal(principal);

    List<FollowingDTO> followingDTOs = user.getFollowingList().stream()
        .filter(follow -> follow.getStatus() == FOLLOWING)
        .map(FollowingDTO::convertToDTO)
        .toList();

    List<FollowerDTO> followerDTOs = user.getFollowerList().stream()
        .filter(follow -> follow.getStatus() == FOLLOWING)
        .map(FollowerDTO::convertToDTO)
        .toList();

    return UserFollowListDTO.builder()
        .followerList(followerDTOs)
        .followingList(followingDTOs)
        .build();
  }

  // 본인 알람 조회
  public Page<Alarm> getAlarmsByUserId(Principal principal, Pageable pageable) {
    String userId = principal.getName();
    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

    Sort defaultSort = Sort.by(Sort.Direction.DESC, "createdTime");
    Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
        defaultSort);

    return alarmRepository.findByUser(user, sortedPageable);
  }

  private User getUserByPrincipal(Principal principal) {
    String userId = principal.getName();
    return userRepository.findByUserId(userId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
  }

  public void validateUserId(String userId) {
    if (userId.length() < 7) {
      throw new CustomException(ErrorCode.INVALID_USER_ID);
    }
  }

  public void validateUserPw(String password) {
    if (password.length() < 7) {
      throw new CustomException(ErrorCode.INVALID_USER_PW);
    }
  }
}
