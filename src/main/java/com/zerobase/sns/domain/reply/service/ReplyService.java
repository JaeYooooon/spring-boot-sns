package com.zerobase.sns.domain.reply.service;

import static com.zerobase.sns.domain.follow.entity.FollowStatus.FOLLOWING;
import static com.zerobase.sns.global.exception.ErrorCode.NOT_FOUND_COMMENT;
import static com.zerobase.sns.global.exception.ErrorCode.NOT_FOUND_REPLY;
import static com.zerobase.sns.global.exception.ErrorCode.NOT_FOUND_USER;
import static com.zerobase.sns.global.exception.ErrorCode.UNAUTHORIZED_ACCESS;

import com.zerobase.sns.domain.comment.entity.Comment;
import com.zerobase.sns.domain.comment.repository.CommentRepository;
import com.zerobase.sns.domain.follow.repository.FollowRepository;
import com.zerobase.sns.domain.post.entity.Post;
import com.zerobase.sns.domain.reply.dto.ReplyCreateDTO;
import com.zerobase.sns.domain.reply.dto.ReplyResDTO;
import com.zerobase.sns.domain.reply.entity.Reply;
import com.zerobase.sns.domain.reply.repository.ReplyRepository;
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
public class ReplyService {

  private final ReplyRepository replyRepository;
  private final UserRepository userRepository;
  private final CommentRepository commentRepository;
  private final FollowRepository followRepository;

  @Transactional
  public ReplyResDTO createReply(Long commentId, ReplyCreateDTO replyCreateDTO,
      Principal principal) {
    String userId = principal.getName();
    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

    Comment parentComment = commentRepository.findById(commentId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_COMMENT));

    Post post = parentComment.getPost();

    boolean canReply = followRepository
        .existsByStatusAndFollowerAndFollowing(FOLLOWING, user, post.getUser());

    if (!canReply) {
      throw new CustomException(UNAUTHORIZED_ACCESS);
    }

    Reply reply = Reply.builder()
        .user(user)
        .parentComment(parentComment)
        .reply(replyCreateDTO.getContent())
        .build();

    Reply savedReply = replyRepository.save(reply);

    return ReplyResDTO.convertToDTO(savedReply);
  }

  @Transactional
  public void deleteReply(Long replyId, Principal principal) {
    String userId = principal.getName();
    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

    Reply reply = replyRepository.findById(replyId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_REPLY));

    if (!Objects.equals(user.getId(), reply.getUser().getId())) {
      throw new CustomException(UNAUTHORIZED_ACCESS);
    }

    replyRepository.delete(reply);
  }
}
