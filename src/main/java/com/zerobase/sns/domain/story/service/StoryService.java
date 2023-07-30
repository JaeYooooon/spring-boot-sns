package com.zerobase.sns.domain.story.service;

import static com.zerobase.sns.global.exception.ErrorCode.NOT_FOUND_STORY;
import static com.zerobase.sns.global.exception.ErrorCode.NOT_FOUND_USER;
import static com.zerobase.sns.global.exception.ErrorCode.UNAUTHORIZED_ACCESS;

import com.zerobase.sns.domain.story.dto.StoryDTO;
import com.zerobase.sns.domain.story.dto.StoryDetailDTO;
import com.zerobase.sns.domain.story.entity.Story;
import com.zerobase.sns.domain.story.repository.StoryRepository;
import com.zerobase.sns.domain.user.entity.User;
import com.zerobase.sns.domain.user.repository.UserRepository;
import com.zerobase.sns.global.exception.CustomException;
import com.zerobase.sns.global.s3.S3Uploader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class StoryService {

  private final StoryRepository storyRepository;
  private final UserRepository userRepository;
  private final S3Uploader s3Uploader;

  @Transactional
  public StoryDetailDTO createStory(StoryDTO storyDTO, MultipartFile multipartFile,
      Principal principal) {
    String userId = principal.getName();
    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

    String image = s3Uploader.fileUpload(multipartFile);

    Story story = new Story();
    story.setUser(user);
    story.setContent(storyDTO.getContent());
    story.setImage(image);
    storyRepository.save(story);

    return StoryDetailDTO.convertToDTO(story);
  }

  @Transactional
  public void deleteStory(Long storyId, Principal principal) {
    String userId = principal.getName();
    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

    Story story = storyRepository.findById(storyId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_STORY));

    if (!story.getUser().equals(user)) {
      throw new CustomException(UNAUTHORIZED_ACCESS);
    }

    String imageUrl = URLDecoder.decode(story.getImage(), StandardCharsets.UTF_8);
    String key = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);

    storyRepository.delete(story);
    s3Uploader.delete(key);
  }

}
