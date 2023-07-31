package com.zerobase.sns.domain.comment.dto;

import com.zerobase.sns.domain.comment.entity.Comment;
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
public class CommentResDTO {

  private String content;

  public static CommentResDTO convertToDTO(Comment comment) {
    return CommentResDTO.builder()
        .content(comment.getContent())
        .build();
  }
}
