package com.zerobase.sns.domain.post.dto;

import com.zerobase.sns.domain.post.entity.Post;
import com.zerobase.sns.domain.tag.dto.TagDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
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
public class PostDTO {

  private String nickName;
  private String title;
  private String content;
  private List<TagDTO> tags;
  private LocalDateTime createdTime;
  private LocalDateTime modifiedTime;

  public static PostDTO convertToDTO(Post post) {
    List<TagDTO> tagDTOs = post.getTags().stream()
        .map(tag -> TagDTO.builder()
            .nickName(tag.getUser().getNickName())
            .build())
        .collect(Collectors.toList());

    return PostDTO.builder()
        .nickName(post.getUser().getNickName())
        .title(post.getTitle())
        .content(post.getContent())
        .tags(tagDTOs)
        .createdTime(post.getCreatedTime())
        .modifiedTime(post.getModifiedTime())
        .build();
  }
}
