package vn.edu.hcmus.fit.learningpath.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private Integer id;
    private Integer postId;
    private Integer authorId;
    private String authorName;
    private String authorAvatarUrl;
    private String content;
    private Integer parentId;
    private LocalDateTime createdAt;
    private List<CommentResponse> replies;
}
