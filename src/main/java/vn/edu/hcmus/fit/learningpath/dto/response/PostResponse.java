package vn.edu.hcmus.fit.learningpath.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private Integer id;
    private Integer forumId;
    private Integer authorId;
    private String authorName;
    private String authorAvatarUrl;
    private String title;
    private String content;
    private String imageUrl;
    private String tags;
    private Integer viewsCount;
    private Integer likesCount;
    private Integer commentsCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
