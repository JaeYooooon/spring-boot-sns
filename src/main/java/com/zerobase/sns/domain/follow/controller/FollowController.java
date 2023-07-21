package com.zerobase.sns.domain.follow.controller;

import com.zerobase.sns.domain.follow.dto.FollowingDTO;
import com.zerobase.sns.domain.follow.entity.Follow;
import com.zerobase.sns.domain.follow.entity.FollowStatus;
import com.zerobase.sns.domain.follow.service.FollowService;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/follow")
public class FollowController {
  private final FollowService followService;

  @PostMapping("/{followingId}")
  public ResponseEntity<FollowStatus> toggleFollow(
      @PathVariable Long followingId,
      Principal principal
  ){
    return ResponseEntity.ok(followService.toggleFollow(followingId, principal));
  }

  @GetMapping("/sent")
  public ResponseEntity<List<FollowingDTO>> getFollowRequestsSentByUser(Principal principal) {
    String userId = principal.getName();

    return ResponseEntity.ok(followService.getFollowRequestsSentByUser(userId));
  }

  @GetMapping("/received")
  public ResponseEntity<List<FollowingDTO>> getFollowRequestsReceivedByUser(Principal principal) {
    String userId = principal.getName();

    return ResponseEntity.ok(followService.getFollowRequestsReceivedByUser(userId));
  }
}
