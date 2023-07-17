package com.zerobase.sns.domain.comment.entity;

import com.zerobase.sns.domain.post.entity.Post;
import com.zerobase.sns.domain.reply.entity.Reply;
import com.zerobase.sns.domain.user.entity.User;
import com.zerobase.sns.global.entity.BaseEntity;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String content;

  @ManyToOne
  @JoinColumn(name = "post_id")
  private Post post;

  @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL)
  private List<Reply> replies;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;
}
