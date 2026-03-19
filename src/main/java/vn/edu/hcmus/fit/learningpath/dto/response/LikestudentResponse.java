package vn.edu.hcmus.fit.learningpath.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikestudentResponse {
    private Integer id;
    private String fullName;
    private String studentCode;
    private String avatarUrl;
}
