package com.zerobase.sns.domain.user.controller;

import com.zerobase.sns.domain.user.dto.UserDTO;
import com.zerobase.sns.domain.user.dto.UserFollowListDTO;
import com.zerobase.sns.domain.user.dto.UserReqDTO;
import com.zerobase.sns.domain.user.dto.UserResDTO;
import com.zerobase.sns.domain.user.dto.UserUpdateDTO;
import com.zerobase.sns.domain.user.service.UserService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
public class UserController {

  private final UserService userService;

  // 회원가입
  @PostMapping("/join")
  public ResponseEntity<UserResDTO> join(@RequestBody UserDTO joinDTO) {

    return ResponseEntity.ok(userService.join(joinDTO));
  }

  // 로그인
  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestBody UserReqDTO loginDTO) {

    return ResponseEntity.ok("Bearer " + userService.login(loginDTO));
  }

  // 회원 탈퇴
  @DeleteMapping
  public void deleteUser(Principal principal) {

    userService.deleteUser(principal);
  }

  // 회원정보 수정
  @PutMapping
  public ResponseEntity<UserResDTO> updateUser(@RequestBody UserUpdateDTO userUpdateDTO,
      Principal principal) {

    return ResponseEntity.ok(userService.updateUser(userUpdateDTO, principal));
  }

  // 공개 범위 설정
  @PutMapping("/privacy")
  public ResponseEntity<UserResDTO> updatePrivacy(@RequestParam("isPrivate") boolean isPrivate,
      Principal principal) {

    return ResponseEntity.ok(userService.updatePrivacy(isPrivate, principal));
  }

  // 본인 정보 조회
  @GetMapping
  public ResponseEntity<UserDTO> getUserDetail(Principal principal) {

    return ResponseEntity.ok(userService.getUserDetail(principal));
  }

  // 본인 팔로우/팔로잉 목록 조회
  @GetMapping("/follow")
  public ResponseEntity<UserFollowListDTO> getFollowList(Principal principal) {

    return ResponseEntity.ok(userService.getFollowList(principal));
  }
}
