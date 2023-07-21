package com.zerobase.sns.domain.follow.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FollowStatus {
  FOLLOWING,
  UNFOLLOWING,
  REQUESTED,
  ACCEPTED,
  REJECTED;

  public static final FollowStatus DEFAULT = ACCEPTED;
}
