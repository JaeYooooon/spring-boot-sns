package com.zerobase.sns.domain.comment.dto;

import com.zerobase.sns.domain.reply.dto.ReplyDTO;
import java.util.List;
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
public class CommentDTO {
  private String content;
  private List<ReplyDTO> replies;
}
