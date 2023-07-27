package com.zerobase.sns.domain.follow.repository;

import com.zerobase.sns.domain.follow.entity.Follow;
import com.zerobase.sns.domain.follow.entity.FollowStatus;
import com.zerobase.sns.domain.user.entity.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

  boolean existsByFollowerAndFollowing(User follower, User following);

  void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);

  Page<Follow> findByFollowerAndStatus(User follower, FollowStatus status, Pageable pageable);

  Page<Follow> findByFollowingAndStatus(User follower, FollowStatus status, Pageable pageable);

  @Query("SELECT f.following FROM Follow f WHERE f.status = :status AND f.follower = :follower")
  List<User> findFollowingUsersByStatusAndFollower(@Param("status") FollowStatus status,
      @Param("follower") User follower);


  boolean existsByStatusAndFollowerAndFollowing(FollowStatus status, User follower, User following);
}
