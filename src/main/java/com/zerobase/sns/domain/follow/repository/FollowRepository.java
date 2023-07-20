package com.zerobase.sns.domain.follow.repository;

import com.zerobase.sns.domain.follow.entity.Follow;
import com.zerobase.sns.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

  boolean existsByFollowerAndFollowing(User follower, User following);

  void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);
}
