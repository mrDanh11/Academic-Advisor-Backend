package vn.edu.hcmus.fit.learningpath.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmus.fit.learningpath.dto.response.*;
import vn.edu.hcmus.fit.learningpath.service.AcademicService;

@RestController
@RequestMapping("/api/v1/academic")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Academic", description = "Operations related to academic progress and credits")
public class AcademicController {

    private final AcademicService academicService;

    @GetMapping("/{studentId}/progress")
    @Operation(summary = "Get academic progress stats", description = "Retrieves total credits earned, required, and cumulative progress by semester.")
    public ApiResponse<DashboardStatsResponse> getAcademicProgress(@PathVariable Integer studentId) {
        return ApiResponse.<DashboardStatsResponse>builder()
                .code(HttpStatus.OK.value())
                .result(academicService.getAcademicProgress(studentId))
                .build();
    }
}
