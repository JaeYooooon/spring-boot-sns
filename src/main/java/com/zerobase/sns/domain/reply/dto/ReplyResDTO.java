package com.zerobase.sns.domain.reply.dto;

import com.zerobase.sns.domain.reply.entity.Reply;
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
public class ReplyResDTO {

  private String reply;

  public static ReplyResDTO convertToDTO(Reply reply) {
    return ReplyResDTO.builder()
        .reply(reply.getReply())
        .build();
  }
}
