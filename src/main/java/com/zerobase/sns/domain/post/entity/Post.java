package com.zerobase.sns.domain.post.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerobase.sns.domain.comment.entity.Comment;
import com.zerobase.sns.domain.likes.entity.Likes;
import com.zerobase.sns.domain.tag.entity.Tag;
import com.zerobase.sns.domain.user.entity.User;
import com.zerobase.sns.global.entity.BaseEntity;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String content;

  @Column(name = "MODIFIED_TIME")
  @LastModifiedDate
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
  private LocalDateTime modifiedTime;

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
  private List<Likes> likes;

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
  private List<Tag> tags;

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
  private List<Comment> comments;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  private int likeCount;

  @PreUpdate
  public void onPreUpdate() {
    this.modifiedTime = LocalDateTime.now();
  }
}
