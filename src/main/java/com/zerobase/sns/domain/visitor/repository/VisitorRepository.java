package com.zerobase.sns.domain.visitor.repository;

import com.zerobase.sns.domain.story.entity.Story;
import com.zerobase.sns.domain.user.entity.User;
import com.zerobase.sns.domain.visitor.entity.Visitor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Long> {

  boolean existsByStoryAndVisitor(Story story, User visitor);

  List<Visitor> findByStory(Story story);
}
