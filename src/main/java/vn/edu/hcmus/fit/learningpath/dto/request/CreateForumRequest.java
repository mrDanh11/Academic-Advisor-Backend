package vn.edu.hcmus.fit.learningpath.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateForumRequest {
    @Schema(description = "Tên forum", example = "Cộng đồng Java HCMUS")
    private String name;

    @Schema(description = "Mô tả forum", example = "Nơi thảo luận về Java và Spring Boot")
    private String description;

    @Schema(description = "Trạng thái công khai", example = "true")
    private boolean isPublic;

    @Schema(description = "ID sinh viên tạo forum", example = "1")
    private Integer authorId;
}
