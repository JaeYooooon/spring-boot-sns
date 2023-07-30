package com.zerobase.sns.domain.story.dto;

import com.zerobase.sns.domain.story.entity.Story;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoryListDTO {

  private String nickName;
  private String content;
  private String image;
  private LocalDateTime createdTime;

  public static Page<StoryListDTO> convertToDTO(Page<Story> postPage) {
    return postPage.map(post -> StoryListDTO.builder()
        .nickName(post.getUser().getNickName())
        .content(post.getContent())
        .image(post.getContent())
        .createdTime(post.getCreatedTime())
        .build());
  }
}
