package com.zerobase.sns.domain.user.service;

import static com.zerobase.sns.global.exception.ErrorCode.ALREADY_EXIST_NICKNAME;
import static com.zerobase.sns.global.exception.ErrorCode.ALREADY_EXIST_USER;
import static com.zerobase.sns.global.exception.ErrorCode.NONE_CORRECT_PW;
import static com.zerobase.sns.global.exception.ErrorCode.NOT_FOUND_USER;

import com.zerobase.sns.domain.user.dto.UserDTO;
import com.zerobase.sns.domain.user.dto.UserFollowListDTO;
import com.zerobase.sns.domain.user.dto.UserReqDTO;
import com.zerobase.sns.domain.user.dto.UserResDTO;
import com.zerobase.sns.domain.user.dto.UserUpdateDTO;
import com.zerobase.sns.domain.user.entity.User;
import com.zerobase.sns.domain.user.repository.UserRepository;
import com.zerobase.sns.global.exception.CustomException;
import com.zerobase.sns.global.security.JwtTokenProvider;
import java.security.Principal;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;


  // 회원 가입
  @Transactional
  public UserResDTO join(UserDTO joinDTO) {

    joinDTO.validateUserId();
    joinDTO.validateUserPw();

    User user = User.builder()
        .userId(joinDTO.getUserId())
        .password(passwordEncoder.encode(joinDTO.getPassword()))
        .nickName(joinDTO.getNickName())
        .isPrivate(joinDTO.getIsPrivate())
        .build();

    if (userRepository.existsByUserId(user.getUserId())) {
      throw new CustomException(ALREADY_EXIST_USER);
    }

    if (userRepository.existsByNickName(user.getNickName())) {
      throw new CustomException(ALREADY_EXIST_NICKNAME);
    }

    userRepository.save(user);

    return UserResDTO.builder()
        .userId(user.getUserId())
        .nickName(user.getNickName())
        .isPrivate(user.getIsPrivate())
        .build();
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
  @Transactional
  public UserResDTO updateUser(UserUpdateDTO userUpdateDTO, Principal principal) {

    userUpdateDTO.validateUserId();
    userUpdateDTO.validateUserPw();

    User user = getUserByPrincipal(principal);

    String newUserId = Optional.ofNullable(userUpdateDTO.getUserId())
        .orElse(user.getUserId());
    String newPassword = Optional.ofNullable(userUpdateDTO.getPassword())
        .orElse(user.getPassword());
    String newNickName = Optional.ofNullable(userUpdateDTO.getNickName())
        .orElse(user.getNickName());

    if (userRepository.existsByNickName(newNickName)) {
      throw new CustomException(ALREADY_EXIST_NICKNAME);
    }

    user.setUserId(newUserId);
    user.setPassword(passwordEncoder.encode(newPassword));
    user.setNickName(newNickName);

    userRepository.save(user);

    return UserResDTO.builder()
        .userId(user.getUserId())
        .nickName(user.getNickName())
        .isPrivate(user.getIsPrivate())
        .build();
  }

  // 공개 범위 설정
  @Transactional
  public UserResDTO updatePrivacy(boolean isPrivate, Principal principal) {
    User user = getUserByPrincipal(principal);

    user.setIsPrivate(isPrivate);
    userRepository.save(user);

    return UserResDTO.builder()
        .userId(user.getUserId())
        .nickName(user.getNickName())
        .isPrivate(user.getIsPrivate())
        .build();
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

    return UserFollowListDTO.builder()
        .followerList(user.getFollowerList())
        .followingList(user.getFollowingList())
        .build();
  }

  private User getUserByPrincipal(Principal principal) {
    String userId = principal.getName();
    return userRepository.findByUserId(userId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
  }
}
