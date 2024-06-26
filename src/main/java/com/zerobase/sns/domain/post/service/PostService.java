package com.zerobase.sns.domain.post.service;

import static com.zerobase.sns.domain.follow.entity.FollowStatus.FOLLOWING;
import static com.zerobase.sns.global.exception.ErrorCode.NOT_FOUND_POST;
import static com.zerobase.sns.global.exception.ErrorCode.NOT_FOUND_USER;
import static com.zerobase.sns.global.exception.ErrorCode.UNAUTHORIZED_ACCESS;

import com.zerobase.sns.domain.alarm.entity.Alarm;
import com.zerobase.sns.domain.alarm.repository.AlarmRepository;
import com.zerobase.sns.domain.follow.repository.FollowRepository;
import com.zerobase.sns.domain.likes.entity.Likes;
import com.zerobase.sns.domain.likes.repository.LikesRepository;
import com.zerobase.sns.domain.post.command.Command;
import com.zerobase.sns.domain.post.command.LikePostCommand;
import com.zerobase.sns.domain.post.dto.PostCreateDTO;
import com.zerobase.sns.domain.post.dto.PostDTO;
import com.zerobase.sns.domain.post.dto.PostDetailDTO;
import com.zerobase.sns.domain.post.dto.PostListDTO;
import com.zerobase.sns.domain.post.dto.PostUpdateDTO;
import com.zerobase.sns.domain.post.entity.Post;
import com.zerobase.sns.domain.post.repository.PostRepository;
import com.zerobase.sns.domain.tag.entity.Tag;
import com.zerobase.sns.domain.tag.repository.TagRepository;
import com.zerobase.sns.domain.user.entity.User;
import com.zerobase.sns.domain.user.repository.UserRepository;
import com.zerobase.sns.global.exception.CustomException;
import java.security.Principal;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final TagRepository tagRepository;
  private final AlarmRepository alarmRepository;
  private final FollowRepository followRepository;
  private final LikesRepository likesRepository;

  private static final Logger logger = LoggerFactory.getLogger(PostService.class);

  // 게시글 작성
  @Transactional
  public PostDTO createPost(PostCreateDTO postCreateDTO, Principal principal) {
    String userId = principal.getName();
    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

    // 닉네임으로 유저 태그
    List<Tag> tags = new ArrayList<>();

    if (postCreateDTO.getTags() != null) {
      for (String nickName : postCreateDTO.getTags()) {
        User taggedUser = userRepository.findByNickName(nickName)
            .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        Tag tag = Tag.builder()
            .user(taggedUser)
            .build();

        tags.add(tag);
      }
    }

    Post post = Post.builder()
        .user(user)
        .title(postCreateDTO.getTitle())
        .content(postCreateDTO.getContent())
        .tags(tags)
        .build();

    Post savedPost = postRepository.save(post);

    // 알람 생성
    for (Tag tag : tags) {
      createAlarm(user, tag.getUser(), savedPost, tag);
      tag.setPost(savedPost);
      tagRepository.save(tag);
    }

    return PostDTO.convertToDTO(savedPost);
  }

  // 게시글 수정
  @Transactional
  public PostDTO updatePost(Long postId, PostUpdateDTO postUpdateDTO, Principal principal) {
    String userId = principal.getName();
    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_POST));

    if (!post.getUser().equals(user)) {
      throw new CustomException(UNAUTHORIZED_ACCESS);
    }

    String title = Optional.ofNullable(postUpdateDTO.getTitle()).orElse(post.getTitle());
    String content = Optional.ofNullable(postUpdateDTO.getContent()).orElse(post.getContent());

    post.setTitle(title);
    post.setContent(content);

    postRepository.save(post);

    return PostDTO.convertToDTO(post);
  }

  // 게시글 삭제
  @Transactional
  public void deletePost(Long postId, Principal principal) {
    String userId = principal.getName();
    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_POST));

    if (!post.getUser().equals(user)) {
      throw new CustomException(UNAUTHORIZED_ACCESS);
    }

    postRepository.delete(post);
  }

  // 게시글 목록
  public Page<PostListDTO> getAllPosts(Principal principal, Pageable pageable) {
    String userId = principal.getName();
    User currentUser = userRepository.findByUserId(userId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

    List<User> followings = followRepository.findFollowingUsersByStatusAndFollower(FOLLOWING,
        currentUser);

    followings.add(currentUser);

    // 최신순 정렬
    Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
        Sort.by(Sort.Direction.DESC, "createdTime"));

    Instant start = Instant.now();
    Page<Post> postPage = postRepository.findByUserIn(followings, sortedPageable);
    Instant end = Instant.now();

    long duration = Duration.between(start, end).toMillis();
    logger.info("time -> " + duration + " ms");

    return PostListDTO.convertToDTO(postPage);
  }

  // 좋아요
  @Transactional
  public void toggleLikePost(Long postId, Principal principal) {
    String userId = principal.getName();
    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_POST));
    Likes existingLike = likesRepository.findByUserAndPost(user, post);

    if (existingLike != null) {
      Command unlikeCommand = new LikePostCommand(likesRepository, user, post, existingLike);
      unlikeCommand.undo();
    } else {
      Command likeCommand = new LikePostCommand(likesRepository, user, post, null);
      likeCommand.execute();
    }
  }

  // 게시글 상세
  public PostDetailDTO getPostById(Long postId, Principal principal) {
    String userId = principal.getName();
    User currentUser = userRepository.findByUserId(userId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_POST));

    if (post.getUser().equals(currentUser) || !post.getUser().getIsPrivate()) {
      return PostDetailDTO.convertToDTO(post);
    }

    boolean isFollowing = followRepository
        .existsByStatusAndFollowerAndFollowing(FOLLOWING, currentUser, post.getUser());

    if (isFollowing) {
      return PostDetailDTO.convertToDTO(post);
    }

    throw new CustomException(UNAUTHORIZED_ACCESS);
  }

  private void createAlarm(User tagger, User taggedUser, Post post, Tag tag) {
    String content = tagger.getNickName() + "님이 당신을 '" + post.getTitle() + "' 글에 태그했습니다.";
    Alarm alarm = Alarm.builder()
        .content(content)
        .user(taggedUser)
        .tag(tag)
        .build();
    alarmRepository.save(alarm);
  }
}
