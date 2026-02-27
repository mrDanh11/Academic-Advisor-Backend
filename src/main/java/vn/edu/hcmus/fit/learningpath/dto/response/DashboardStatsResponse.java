package vn.edu.hcmus.fit.learningpath.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Academic progress statistics for dashboard")
public class DashboardStatsResponse {
    @Schema(description = "Total credits earned by the student", example = "120")
    private Integer totalCreditsEarned;
    
    @Schema(description = "Required credits for graduation", example = "138")
    private Integer totalCreditsRequired;
    
    @Schema(description = "Credits completed so far", example = "120")
    private Integer creditsCompleted;
    
    @Schema(description = "Graduation progress percentage", example = "86.9")
    private Double graduationProgress;
    
    @Schema(description = "List of cumulative credits by semester for chart visualization")
    private List<SemesterProgressResponse> semesterProgress;

    @Data
    @Builder
    @Schema(description = "Credits progress for a specific semester")
    public static class SemesterProgressResponse {
        @Schema(description = "Semester identifier", example = "2023.1")
        private String semester;
        
        @Schema(description = "Credits earned in this semester", example = "15")
        private Integer creditsEarned;
        
        @Schema(description = "Cumulative credits up to this semester", example = "45")
        private Integer cumulativeCredits;
    }
}