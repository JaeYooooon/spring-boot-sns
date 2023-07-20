package com.zerobase.sns.domain.user.dto;

import com.zerobase.sns.global.exception.CustomException;
import com.zerobase.sns.global.exception.ErrorCode;
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
public class UserDTO {

  private String userId;
  private String password;
  private String nickName;
  private boolean isPrivate;

  public boolean getIsPrivate() {
    return isPrivate;
  }
}
