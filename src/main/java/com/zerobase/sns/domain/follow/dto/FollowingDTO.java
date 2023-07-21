package com.zerobase.sns.domain.follow.dto;

import com.zerobase.sns.domain.follow.entity.Follow;
import com.zerobase.sns.domain.follow.entity.FollowStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowingDTO {

  private Long id;
  private Long followingId;
  private FollowStatus status;

  public static FollowingDTO convertToDTO(Follow follow) {
    return FollowingDTO.builder()
        .id(follow.getId())
        .followingId(follow.getFollowing().getId())
        .status(follow.getStatus())
        .build();
  }
}
