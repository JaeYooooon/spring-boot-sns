package com.zerobase.sns.domain.reply.entity;

import com.zerobase.sns.domain.comment.entity.Comment;
import com.zerobase.sns.domain.user.entity.User;
import com.zerobase.sns.global.entity.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Reply extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String reply;

  @ManyToOne
  @JoinColumn(name = "comment_id")
  private Comment parentComment;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;
}
