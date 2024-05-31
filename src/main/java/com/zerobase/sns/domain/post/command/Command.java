package com.zerobase.sns.domain.post.command;

public interface Command {
  void execute();
  void undo();
}
