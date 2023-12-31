package com.zerobase.sns.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.zerobase.sns.domain.follow.entity.Follow;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String userId;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false, unique = true)
  private String nickName;

  private Boolean isPrivate;

  @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL)
  @JsonBackReference
  private List<Follow> followingList;

  @OneToMany(mappedBy = "following", cascade = CascadeType.ALL)
  @JsonBackReference
  private List<Follow> followerList;
}
