package com.zerobase.sns.domain.user.dto;

import com.zerobase.sns.domain.user.entity.User;
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
public class UserResDTO {

  private String userId;
  private String nickName;
  private Boolean isPrivate;

  public static UserResDTO fromUser(User user){
    return new UserResDTO(
        user.getUserId(),
        user.getNickName(),
        user.getIsPrivate()
    );
  }
}
