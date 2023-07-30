package com.zerobase.sns.domain.story.dto;

import com.zerobase.sns.domain.story.entity.Story;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoryDetailDTO {

  private String nickName;
  private String content;
  private String image;
  private LocalDateTime createdTime;

  public static StoryDetailDTO convertToDTO(Story story) {
    return StoryDetailDTO.builder()
        .nickName(story.getUser().getNickName())
        .content(story.getContent())
        .image(story.getImage())
        .createdTime(story.getCreatedTime())
        .build();
  }
}
