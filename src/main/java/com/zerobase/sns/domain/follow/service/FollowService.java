package com.zerobase.sns.domain.follow.service;

import static com.zerobase.sns.domain.follow.entity.FollowStatus.FOLLOWING;
import static com.zerobase.sns.domain.follow.entity.FollowStatus.REQUESTED;
import static com.zerobase.sns.domain.follow.entity.FollowStatus.UNFOLLOWING;
import static com.zerobase.sns.global.exception.ErrorCode.CANNOT_FOLLOW_YOURSELF;
import static com.zerobase.sns.global.exception.ErrorCode.FOLLOW_REQUEST_ALREADY_ACCEPTED;
import static com.zerobase.sns.global.exception.ErrorCode.NOT_FOUND_USER;
import static com.zerobase.sns.global.exception.ErrorCode.UNAUTHORIZED_ACCESS;

import com.zerobase.sns.domain.follow.dto.FollowingDTO;
import com.zerobase.sns.domain.follow.entity.Follow;
import com.zerobase.sns.domain.follow.entity.FollowStatus;
import com.zerobase.sns.domain.follow.repository.FollowRepository;
import com.zerobase.sns.domain.user.entity.User;
import com.zerobase.sns.domain.user.repository.UserRepository;
import com.zerobase.sns.global.exception.CustomException;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowService {

  private final FollowRepository followRepository;
  private final UserRepository userRepository;

  @Transactional
  public FollowStatus toggleFollow(Long followingId, Principal principal) {
    User follower = userRepository.findByUserId(principal.getName())
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
    User following = userRepository.findById(followingId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

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

  public Page<FollowingDTO> getFollowRequestsSentByUser(Principal principal, Pageable pageable) {
    String userId = principal.getName();
    User follower = userRepository.findByUserId(userId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

    Page<Follow> followRequestsSent = followRepository.findByFollowerAndStatus(follower, REQUESTED,
        pageable);

    return followRequestsSent.map(FollowingDTO::convertToDTO);
  }

  public Page<FollowingDTO> getFollowRequestsReceivedByUser(Principal principal,
      Pageable pageable) {
    String userId = principal.getName();
    User following = userRepository.findByUserId(userId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

    Page<Follow> followRequestsReceived = followRepository.findByFollowingAndStatus(following,
        REQUESTED, pageable);

    return followRequestsReceived.map(FollowingDTO::convertToDTO);
  }

  @Transactional
  public FollowStatus acceptFollowRequest(Long followerId, Principal principal) {
    String userId = principal.getName();
    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

    Follow followRequest = followRepository.findById(followerId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

    if (!followRequest.getFollowing().equals(user)) {
      throw new CustomException(UNAUTHORIZED_ACCESS);
    }

    if (followRequest.getStatus() == FOLLOWING) {
      throw new CustomException(FOLLOW_REQUEST_ALREADY_ACCEPTED);
    }

    followRequest.setStatus(FOLLOWING);
    followRepository.save(followRequest);

    return FOLLOWING;
  }

  @Transactional
  public void rejectFollowRequest(Long followingId, Principal principal) {
    String userId = principal.getName();
    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

    Follow followRequest = followRepository.findById(followingId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

    if (!followRequest.getFollowing().equals(user)) {
      throw new CustomException(UNAUTHORIZED_ACCESS);
    }

    followRepository.delete(followRequest);
  }
}
