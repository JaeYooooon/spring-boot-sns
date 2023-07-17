package com.zerobase.sns.domain.visitor.entity;

import com.zerobase.sns.domain.story.entity.Story;
import com.zerobase.sns.domain.user.entity.User;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Visitor {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "story_id")
  private Story story;

  @ManyToOne
  @JoinColumn(name = "visitor_id")
  private User visitor;
}
