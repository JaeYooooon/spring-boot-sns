package com.zerobase.sns.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
  // 회원
  ALREADY_EXIST_USER(HttpStatus.BAD_REQUEST, "이미 등록 되어있는 사용자입니다. "),
  NONE_CORRECT_PW(HttpStatus.BAD_REQUEST, "패스워드가 일치하지 않습니다. "),
  NOT_FOUND_USER(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
  INVALID_USER_ID(HttpStatus.BAD_REQUEST, "아이디는 8글자 이상이어야 합니다. "),
  INVALID_USER_PW(HttpStatus.BAD_REQUEST, "비밀번호는 8글자 이상이어야 합니다. "),
  ALREADY_EXIST_NICKNAME(HttpStatus.BAD_REQUEST, "이미 등록된 닉네임입니다. "),;

  private final HttpStatus httpStatus;
  private final String detail;
}
