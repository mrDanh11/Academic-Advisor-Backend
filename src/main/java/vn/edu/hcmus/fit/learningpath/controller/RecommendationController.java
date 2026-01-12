package vn.edu.hcmus.fit.learningpath.controller;

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
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping("/{studentId}")
    public ApiResponse<List<RecommendationResponse>> getRecommendations(@PathVariable Integer studentId) {
        return ApiResponse.<List<RecommendationResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(recommendationService.getRecommendations(studentId))
                .build();
    }
}
