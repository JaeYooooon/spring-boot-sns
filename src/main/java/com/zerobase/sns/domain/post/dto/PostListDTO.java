package com.zerobase.sns.domain.post.dto;

import com.zerobase.sns.domain.post.entity.Post;
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
public class PostListDTO {

  private String nickName;
  private String title;
  private int likeCount;
  private int commentCount;
  private LocalDateTime createdTime;
  private LocalDateTime modifiedTime;

  public static Page<PostListDTO> convertToDTO(Page<Post> postPage) {
    return postPage.map(post -> PostListDTO.builder()
        .nickName(post.getUser().getNickName())
        .title(post.getTitle())
        .likeCount(post.getLikeCount())
        .commentCount(post.getCommentCount())
        .createdTime(post.getCreatedTime())
        .modifiedTime(post.getModifiedTime())
        .build());
  }
}
