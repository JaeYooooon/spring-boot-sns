package com.zerobase.sns.domain.story.controller;

import com.zerobase.sns.domain.story.dto.StoryDTO;
import com.zerobase.sns.domain.story.dto.StoryDetailDTO;
import com.zerobase.sns.domain.story.dto.StoryListDTO;
import com.zerobase.sns.domain.story.service.StoryService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/story")
public class StoryController {

  private final StoryService storyService;

  // 스토리 작성
  @PostMapping
  public ResponseEntity<StoryDetailDTO> createStory(StoryDTO storyDTO,
      @RequestPart(value = "image", required = false) MultipartFile multipartFile,
      Principal principal) {

    StoryDetailDTO story = storyService.createStory(storyDTO, multipartFile, principal);

    return ResponseEntity.ok(story);
  }

  // 스토리 삭제
  @DeleteMapping("/{storyId}")
  public void deleteStory(@PathVariable Long storyId,
      Principal principal) {

    storyService.deleteStory(storyId, principal);
  }

  // 스토리 상세
  @GetMapping("/{storyId}")
  public ResponseEntity<StoryDetailDTO> getStoryDetail(@PathVariable Long storyId,
      Principal principal) {

    StoryDetailDTO story = storyService.getStoryDetail(storyId, principal);

    return ResponseEntity.ok(story);
  }

  // 스토리 전체 목록
  @GetMapping
  public ResponseEntity<Page<StoryListDTO>> getAllStory(Principal principal, Pageable pageable) {

    Page<StoryListDTO> storyList = storyService.getAllStory(principal, pageable);

    return ResponseEntity.ok(storyList);
  }
}
