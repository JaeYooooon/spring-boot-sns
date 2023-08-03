package com.zerobase.sns.domain.post.dto;

import com.zerobase.sns.domain.comment.dto.CommentDTO;
import com.zerobase.sns.domain.comment.entity.Comment;
import com.zerobase.sns.domain.likes.dto.LikesDTO;
import com.zerobase.sns.domain.post.entity.Post;
import com.zerobase.sns.domain.reply.dto.ReplyDTO;
import com.zerobase.sns.domain.tag.dto.TagDTO;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDetailDTO {

  private String nickName;
  private String title;
  private String content;
  private List<TagDTO> tags;
  private List<LikesDTO> likes;
  private List<CommentDTO> comments;
  private LocalDateTime createdTime;
  private LocalDateTime modifiedTime;

  public static PostDetailDTO convertToDTO(Post post) {
    List<TagDTO> tagDTOs = post.getTags().stream()
        .map(tag -> TagDTO.builder()
            .nickName(tag.getUser().getNickName())
            .build())
        .collect(Collectors.toList());

    List<LikesDTO> likesDTOS = post.getLikes().stream()
        .map(likes -> LikesDTO.builder()
            .nickName(likes.getUser().getNickName())
            .build())
        .collect(Collectors.toList());

    List<CommentDTO> commentDTOS = post.getComments().stream()
        .sorted(Comparator.comparing(Comment::getCreatedTime).reversed())
        .map(comment -> {
          List<ReplyDTO> replyDTOS = comment.getReplies().stream()
              .map(reply -> ReplyDTO.builder()
                  .nickName(reply.getUser().getNickName())
                  .reply(reply.getReply())
                  .createdTime(reply.getCreatedTime())
                  .build())
              .collect(Collectors.toList());

          return CommentDTO.builder()
              .nickName(comment.getUser().getNickName())
              .content(comment.getContent())
              .createdTime(comment.getCreatedTime())
              .replies(replyDTOS)
              .build();
        })
        .collect(Collectors.toList());

    return PostDetailDTO.builder()
        .nickName(post.getUser().getNickName())
        .title(post.getTitle())
        .content(post.getContent())
        .tags(tagDTOs)
        .likes(likesDTOS)
        .comments(commentDTOS)
        .createdTime(post.getCreatedTime())
        .modifiedTime(post.getModifiedTime())
        .build();
  }
}
