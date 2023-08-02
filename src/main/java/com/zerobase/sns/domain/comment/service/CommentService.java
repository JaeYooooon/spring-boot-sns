package com.zerobase.sns.domain.comment.service;

import static com.zerobase.sns.domain.follow.entity.FollowStatus.FOLLOWING;
import static com.zerobase.sns.global.exception.ErrorCode.NOT_FOUND_COMMENT;
import static com.zerobase.sns.global.exception.ErrorCode.NOT_FOUND_POST;
import static com.zerobase.sns.global.exception.ErrorCode.NOT_FOUND_USER;
import static com.zerobase.sns.global.exception.ErrorCode.UNAUTHORIZED_ACCESS;

import com.zerobase.sns.domain.comment.dto.CommentCreateDTO;
import com.zerobase.sns.domain.comment.dto.CommentResDTO;
import com.zerobase.sns.domain.comment.entity.Comment;
import com.zerobase.sns.domain.comment.repository.CommentRepository;
import com.zerobase.sns.domain.follow.repository.FollowRepository;
import com.zerobase.sns.domain.post.entity.Post;
import com.zerobase.sns.domain.post.repository.PostRepository;
import com.zerobase.sns.domain.user.entity.User;
import com.zerobase.sns.domain.user.repository.UserRepository;
import com.zerobase.sns.global.exception.CustomException;
import java.security.Principal;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final UserRepository userRepository;
  private final PostRepository postRepository;
  private final CommentRepository commentRepository;
  private final FollowRepository followRepository;

  @Transactional
  public CommentResDTO createComment(Long postId, CommentCreateDTO commentCreateDTO,
      Principal principal) {

    String userId = principal.getName();
    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_POST));

    if (!Objects.equals(user.getId(), post.getUser().getId())) {
      boolean isFollowing = followRepository
          .existsByStatusAndFollowerAndFollowing(FOLLOWING, user, post.getUser());
      if (!isFollowing) {
        throw new CustomException(UNAUTHORIZED_ACCESS);
      }
    }

    Comment comment = Comment.builder()
        .user(user)
        .post(post)
        .content(commentCreateDTO.getContent())
        .build();

    Comment savedComment = commentRepository.save(comment);

    return CommentResDTO.convertToDTO(savedComment);
  }

  @Transactional
  public void deleteComment(Long commentId, Principal principal) {
    String userId = principal.getName();
    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_COMMENT));

    if (!Objects.equals(user.getId(), comment.getUser().getId())
        && !Objects.equals(user.getId(), comment.getPost().getUser().getId())) {
      throw new CustomException(UNAUTHORIZED_ACCESS);
    }

    commentRepository.delete(comment);
  }
}
