package com.zerobase.sns.domain.user;

import com.zerobase.sns.domain.follow.Follow;
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

@Getter
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
  private List<Follow> followingList;

  @OneToMany(mappedBy = "following", cascade = CascadeType.ALL)
  private List<Follow> followerList;
}
