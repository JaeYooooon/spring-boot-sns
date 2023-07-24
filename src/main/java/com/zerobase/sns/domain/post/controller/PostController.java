package com.zerobase.sns.domain.post.controller;

import com.zerobase.sns.domain.post.dto.PostCreateDTO;
import com.zerobase.sns.domain.post.dto.PostDTO;
import com.zerobase.sns.domain.post.entity.Post;
import com.zerobase.sns.domain.post.service.PostService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/post")
public class PostController {

  private final PostService postService;

  // 게시글 작성
  @PostMapping
  public ResponseEntity<PostDTO> createPost(@RequestBody PostCreateDTO postCreateDTO,
      Principal principal) {

    Post post = postService.createPost(postCreateDTO, principal);

    return ResponseEntity.ok(PostDTO.convertToDTO(post));
  }
}
