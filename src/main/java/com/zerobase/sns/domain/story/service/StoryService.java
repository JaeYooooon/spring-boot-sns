package com.zerobase.sns.domain.story.service;

import static com.zerobase.sns.domain.follow.entity.FollowStatus.FOLLOWING;
import static com.zerobase.sns.global.exception.ErrorCode.NOT_FOUND_STORY;
import static com.zerobase.sns.global.exception.ErrorCode.NOT_FOUND_USER;
import static com.zerobase.sns.global.exception.ErrorCode.UNAUTHORIZED_ACCESS;

import com.zerobase.sns.domain.follow.repository.FollowRepository;
import com.zerobase.sns.domain.story.dto.StoryDTO;
import com.zerobase.sns.domain.story.dto.StoryDetailDTO;
import com.zerobase.sns.domain.story.dto.StoryListDTO;
import com.zerobase.sns.domain.story.entity.Story;
import com.zerobase.sns.domain.story.repository.StoryRepository;
import com.zerobase.sns.domain.user.entity.User;
import com.zerobase.sns.domain.user.repository.UserRepository;
import com.zerobase.sns.domain.visitor.entity.Visitor;
import com.zerobase.sns.domain.visitor.repository.VisitorRepository;
import com.zerobase.sns.global.exception.CustomException;
import com.zerobase.sns.global.s3.S3Uploader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class StoryService {

  private final StoryRepository storyRepository;
  private final UserRepository userRepository;
  private final S3Uploader s3Uploader;
  private final FollowRepository followRepository;
  private final VisitorRepository visitorRepository;

  // 스토리 작성
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

  // 스토리 삭제
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

  // 스토리 상세
  public StoryDetailDTO getStoryDetail(Long storyId, Principal principal) {
    String userId = principal.getName();
    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
    Story story = storyRepository.findById(storyId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_STORY));

    boolean visitExists = visitorRepository.existsByStoryAndVisitor(story, user);

    // 방문자 히스토리
    if (!visitExists) {
      Visitor visitor = Visitor.builder()
          .story(story)
          .visitor(user)
          .build();

      visitorRepository.save(visitor);
    }

    if (story.getUser().equals(user) || !story.getUser().getIsPrivate()) {
      return StoryDetailDTO.convertToDTO(story);
    }

    boolean isFollowing = followRepository
        .existsByStatusAndFollowerAndFollowing(FOLLOWING, user, story.getUser());

    if (isFollowing) {
      StoryDetailDTO.convertToDTO(story);
    }

    throw new CustomException(UNAUTHORIZED_ACCESS);
  }

  // 스토리 전체 목록
  public Page<StoryListDTO> getAllStory(Principal principal, Pageable pageable) {
    String userId = principal.getName();
    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

    List<User> followings = followRepository.findFollowingUsersByStatusAndFollower(FOLLOWING, user);

    followings.add(user);

    // 최신순 정렬
    Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
        Sort.by(Sort.Direction.DESC, "createdTime"));

    Page<Story> storyPage = storyRepository.findByUserIn(followings, sortedPageable);

    return StoryListDTO.convertToDTO(storyPage);
  }

  // 방문자 목록
  public List<String> getStoryVisitor(Long storyId, Principal principal) {
    String userId = principal.getName();
    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
    Story story = storyRepository.findById(storyId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_STORY));

    if (!story.getUser().equals(user)) {
      throw new CustomException(UNAUTHORIZED_ACCESS);
    }

    List<Visitor> visitors = visitorRepository.findByStory(story);

    return visitors.stream()
        .map(Visitor::getVisitor)
        .map(User::getNickName)
        .collect(Collectors.toList());
  }

  // 24시간 지난 스토리 삭제
  public void deleteExpiredStory() {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime expiredTime = now.minusDays(1);

    List<Story> storyList = storyRepository.findByCreatedTimeBefore(expiredTime);

    for (Story story : storyList) {
      String imageUrl = URLDecoder.decode(story.getImage(), StandardCharsets.UTF_8);
      String key = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);

      storyRepository.delete(story);
      s3Uploader.delete(key);
    }
  }
}
