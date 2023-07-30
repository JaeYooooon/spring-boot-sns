package com.zerobase.sns.domain.story.controller;

import com.zerobase.sns.domain.story.dto.StoryDTO;
import com.zerobase.sns.domain.story.dto.StoryDetailDTO;
import com.zerobase.sns.domain.story.service.StoryService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

  @PostMapping
  public ResponseEntity<StoryDetailDTO> createStory(StoryDTO storyDTO,
      @RequestPart(value = "image", required = false) MultipartFile multipartFile,
      Principal principal) {

    return ResponseEntity.ok(storyService.createStory(storyDTO, multipartFile, principal));
  }

  @DeleteMapping("/{storyId}")
  public void deleteStory(@PathVariable Long storyId,
      Principal principal) {

    storyService.deleteStory(storyId, principal);
  }
}
