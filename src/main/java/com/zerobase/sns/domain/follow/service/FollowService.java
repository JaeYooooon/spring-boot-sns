package com.zerobase.sns.domain.follow.service;

import static com.zerobase.sns.global.exception.ErrorCode.CANNOT_FOLLOW_YOURSELF;
import static com.zerobase.sns.global.exception.ErrorCode.NOT_FOUND_USER;

import com.zerobase.sns.domain.follow.entity.Follow;
import com.zerobase.sns.domain.follow.entity.FollowStatus;
import com.zerobase.sns.domain.follow.repository.FollowRepository;
import com.zerobase.sns.domain.user.entity.User;
import com.zerobase.sns.domain.user.repository.UserRepository;
import com.zerobase.sns.global.exception.CustomException;
import java.security.Principal;
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
      return FollowStatus.UNFOLLOWING;
    } else {
      if (following.getIsPrivate()) {
        Follow followRequest = new Follow();
        followRequest.setFollower(follower);
        followRequest.setFollowing(following);
        followRequest.setStatus(FollowStatus.REQUESTED);

        followRepository.save(followRequest);

        return FollowStatus.REQUESTED;
      }
      Follow follow = new Follow();
      follow.setFollower(follower);
      follow.setFollowing(following);
      follow.setStatus(FollowStatus.FOLLOWING);

      followRepository.save(follow);

      return FollowStatus.FOLLOWING;
    }
  }
}
