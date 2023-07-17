package com.zerobase.sns.domain.tag.entity;

import com.zerobase.sns.domain.alarm.entity.Alarm;
import com.zerobase.sns.domain.post.entity.Post;
import com.zerobase.sns.domain.user.entity.User;
import com.zerobase.sns.global.entity.BaseEntity;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Tag extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "post_id")
  private Post post;

  @OneToOne(mappedBy = "tag", cascade = CascadeType.ALL)
  private Alarm alarm;
}
