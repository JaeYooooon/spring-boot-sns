package com.zerobase.sns.domain.user.dto;

import com.zerobase.sns.domain.follow.dto.FollowerDTO;
import com.zerobase.sns.domain.follow.dto.FollowingDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFollowListDTO {

  private List<FollowingDTO> followingList;
  private List<FollowerDTO> followerList;
}
