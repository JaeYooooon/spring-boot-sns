package com.zerobase.sns.domain.comment.controller;

import com.zerobase.sns.domain.comment.dto.CommentCreateDTO;
import com.zerobase.sns.domain.comment.dto.CommentResDTO;
import com.zerobase.sns.domain.comment.service.CommentService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/comment")
public class CommentController {

  private final CommentService commentService;

  @PostMapping("/{postId}")
  public ResponseEntity<CommentResDTO> createComment(
      @PathVariable Long postId,
      @RequestBody CommentCreateDTO commentCreateDTO,
      Principal principal
  ) {
    CommentResDTO createdComment = commentService.createComment(postId, commentCreateDTO,
        principal);
    return ResponseEntity.ok(createdComment);
  }
}
