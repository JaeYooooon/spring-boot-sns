package com.zerobase.sns.domain.likes.repository;

import com.zerobase.sns.domain.likes.entity.Likes;
import com.zerobase.sns.domain.post.entity.Post;
import com.zerobase.sns.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {

  Likes findByUserAndPost(User user, Post post);
}
