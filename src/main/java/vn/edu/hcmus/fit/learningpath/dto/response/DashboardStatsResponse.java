package vn.edu.hcmus.fit.learningpath.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStatsResponse {
    private Integer totalCreditsEarned;
    private Integer totalCreditsRequired;
    private Integer creditsCompleted;
    private Double graduationProgress;
    private List<SemesterProgressResponse> semesterProgress;

    @Data
    @Builder
    public static class SemesterProgressResponse {
        private String semester;
        private Integer creditsEarned;
        private Integer cumulativeCredits;
    }
}