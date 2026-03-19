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
    private String type;
    private Integer authorId;
    private String authorName;
    private String authorAvatarUrl;
    private Integer membersCount;
    private String avatarUrl;
    private LocalDateTime createdAt;
}
