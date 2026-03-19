package vn.edu.hcmus.fit.learningpath.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentRequest {
    @NotNull(message = "Post ID is required")
    private Integer postId;

    @NotNull(message = "Student ID is required")
    private Integer studentId;

    @NotBlank(message = "Content is required")
    private String content;

    private Integer parentId; // Optional for replies
}
