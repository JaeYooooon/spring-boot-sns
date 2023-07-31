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
  ALREADY_EXIST_NICKNAME(HttpStatus.BAD_REQUEST, "이미 등록된 닉네임입니다. "),

  // 팔로우
  CANNOT_FOLLOW_YOURSELF(HttpStatus.BAD_REQUEST, "자기 자신은 팔로우할 수 없습니다."),
  FOLLOW_REQUEST_ALREADY_ACCEPTED(HttpStatus.BAD_REQUEST, "이미 수락되었습니다."),
  FOLLOW_REQUEST_ALREADY_REJECTED(HttpStatus.BAD_REQUEST, "이미 거절되었습니다."),

  // 게시글
  NOT_FOUND_POST(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),

  // 스토리
  NOT_FOUND_STORY(HttpStatus.NOT_FOUND, "스토리를 찾을 수 없습니다."),

  // 댓글
  NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),

  // 권한
  UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "권한이 없습니다."),

  // S3
  S3_UPLOAD_ERROR(HttpStatus.BAD_REQUEST, "파일 업로드 중 오류가 발생하였습니다. "),
  S3_DELETE_ERROR(HttpStatus.BAD_REQUEST, "파일 삭제 중 오류가 발생하였습니다. ");


  private final HttpStatus httpStatus;
  private final String detail;
}
