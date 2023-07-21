package com.zerobase.sns.domain.follow.service;

import static com.zerobase.sns.domain.follow.entity.FollowStatus.*;
import static com.zerobase.sns.global.exception.ErrorCode.CANNOT_FOLLOW_YOURSELF;
import static com.zerobase.sns.global.exception.ErrorCode.NOT_FOUND_USER;

import com.zerobase.sns.domain.follow.dto.FollowingDTO;
import com.zerobase.sns.domain.follow.entity.Follow;
import com.zerobase.sns.domain.follow.entity.FollowStatus;
import com.zerobase.sns.domain.follow.repository.FollowRepository;
import com.zerobase.sns.domain.user.entity.User;
import com.zerobase.sns.domain.user.repository.UserRepository;
import com.zerobase.sns.global.exception.CustomException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowService {

  private final FollowRepository followRepository;
  private final UserRepository userRepository;

  @Transactional
  public FollowStatus toggleFollow(Long followingId, Principal principal) {
    User follower = userRepository.findByUserId(principal.getName()).orElseThrow(() -> new CustomException(NOT_FOUND_USER));
    User following = userRepository.findById(followingId).orElseThrow(() -> new CustomException(NOT_FOUND_USER));

    if (follower.getId().equals(following.getId())) {
      throw new CustomException(CANNOT_FOLLOW_YOURSELF);
    }

    if (followRepository.existsByFollowerAndFollowing(follower, following)) {
      followRepository.deleteByFollowerIdAndFollowingId(follower.getId(), following.getId());
      return UNFOLLOWING;
    } else {
      if (following.getIsPrivate()) {
        Follow followRequest = new Follow();
        followRequest.setFollower(follower);
        followRequest.setFollowing(following);
        followRequest.setStatus(REQUESTED);

        followRepository.save(followRequest);

        return REQUESTED;
      }
      Follow follow = new Follow();
      follow.setFollower(follower);
      follow.setFollowing(following);
      follow.setStatus(FOLLOWING);

      followRepository.save(follow);

      return FOLLOWING;
    }
  }

  public List<FollowingDTO> getFollowRequestsSentByUser(String userId) {
    User follower = userRepository.findByUserId(userId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

    List<Follow> followRequestsSent = followRepository.findByFollowerAndStatus(follower, REQUESTED);

    return followRequestsSent.stream()
        .map(FollowingDTO::convertToDTO)
        .collect(Collectors.toList());
  }

  public List<FollowingDTO> getFollowRequestsReceivedByUser(String userId) {
    User following = userRepository.findByUserId(userId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

    List<Follow> followRequestsReceived = followRepository.findByFollowingAndStatus(following, REQUESTED);

    return followRequestsReceived.stream()
        .map(FollowingDTO::convertToDTO)
        .collect(Collectors.toList());
  }
}
