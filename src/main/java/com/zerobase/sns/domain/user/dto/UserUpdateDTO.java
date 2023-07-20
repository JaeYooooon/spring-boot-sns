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
public class UserUpdateDTO {

  private String userId;
  private String password;
  private String nickName;

  public void validateUserId() {
    if (this.userId.length() < 7) {
      throw new CustomException(ErrorCode.INVALID_USER_ID);
    }
  }

  public void validateUserPw() {
    if (this.password.length() < 7) {
      throw new CustomException(ErrorCode.INVALID_USER_PW);
    }
  }
}
