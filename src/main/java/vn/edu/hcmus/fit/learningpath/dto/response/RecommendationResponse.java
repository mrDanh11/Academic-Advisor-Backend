package vn.edu.hcmus.fit.learningpath.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendationResponse {
    private Integer id;
    private String title;
    private String description;
    private String category;
    private String actionUrl;
}
