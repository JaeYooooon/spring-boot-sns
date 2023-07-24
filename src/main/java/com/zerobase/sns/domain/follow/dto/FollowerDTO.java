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
public class FollowerDTO {

  private Long id;
  private Long followerId;
  private FollowStatus status;

  public static FollowerDTO convertToDTO(Follow follow) {
    return FollowerDTO.builder()
        .id(follow.getId())
        .followerId(follow.getFollower().getId())
        .status(follow.getStatus())
        .build();
  }
}
