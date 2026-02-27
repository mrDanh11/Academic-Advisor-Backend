package vn.edu.hcmus.fit.learningpath.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmus.fit.learningpath.dto.response.*;
import vn.edu.hcmus.fit.learningpath.service.RecommendationService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recommendations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Recommendations", description = "AI-powered course and career path recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping("/{studentId}")
    @Operation(summary = "Get student recommendations", description = "Retrieves a list of AI-generated recommendations for a specific student.")
    public ApiResponse<List<RecommendationResponse>> getRecommendations(@PathVariable Integer studentId) {
        return ApiResponse.<List<RecommendationResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(recommendationService.getRecommendations(studentId))
                .build();
    }
}
