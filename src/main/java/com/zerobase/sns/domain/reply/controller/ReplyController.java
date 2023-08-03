package com.zerobase.sns.domain.reply.controller;

import com.zerobase.sns.domain.reply.dto.ReplyCreateDTO;
import com.zerobase.sns.domain.reply.dto.ReplyResDTO;
import com.zerobase.sns.domain.reply.service.ReplyService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/reply")
public class ReplyController {

  private final ReplyService replyService;

  @PostMapping("/{commentId}")
  public ResponseEntity<ReplyResDTO> createReply(
      @PathVariable Long commentId,
      @RequestBody ReplyCreateDTO replyCreateDTO,
      Principal principal
  ) {
    ReplyResDTO reply = replyService.createReply(commentId, replyCreateDTO, principal);
    return ResponseEntity.ok(reply);
  }

  @DeleteMapping("/{replyId}")
  public void deleteReply(@PathVariable Long replyId, Principal principal) {

    replyService.deleteReply(replyId, principal);
  }
}
