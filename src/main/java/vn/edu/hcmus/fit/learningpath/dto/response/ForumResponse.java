package vn.edu.hcmus.fit.learningpath.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForumResponse {
    private Integer id;
    private String name;
    private String description;
    private String rules;
    private Boolean isPublic;
    private LocalDateTime createdAt;
}
