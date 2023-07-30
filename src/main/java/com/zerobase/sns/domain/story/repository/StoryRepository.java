package com.zerobase.sns.domain.story.repository;

import com.zerobase.sns.domain.post.entity.Post;
import com.zerobase.sns.domain.story.entity.Story;
import com.zerobase.sns.domain.user.entity.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {
  Page<Story> findByUserIn(List<User> followingUsers, Pageable pageable);
}
