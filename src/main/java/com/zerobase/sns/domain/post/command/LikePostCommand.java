package com.zerobase.sns.domain.post.command;

import com.zerobase.sns.domain.likes.entity.Likes;
import com.zerobase.sns.domain.likes.repository.LikesRepository;
import com.zerobase.sns.domain.post.entity.Post;
import com.zerobase.sns.domain.user.entity.User;

public class LikePostCommand implements Command {
  private LikesRepository likesRepository;
  private User user;
  private Post post;
  private Likes like;

  public LikePostCommand(LikesRepository likesRepository, User user, Post post, Likes like) {
    this.likesRepository = likesRepository;
    this.user = user;
    this.post = post;
    this.like = like;
  }

  @Override
  public void execute() {
    like = Likes.builder()
        .user(user)
        .post(post)
        .isLike(true)
        .build();
    likesRepository.save(like);
  }

  @Override
  public void undo() {
    if (like != null) {
      likesRepository.delete(like);
    }
  }
}

