package vn.edu.hcmus.fit.learningpath.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmus.fit.learningpath.dto.response.*;
import vn.edu.hcmus.fit.learningpath.service.AcademicService;

@RestController
@RequestMapping("/api/v1/academic")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AcademicController {

    private final AcademicService academicService;

    @GetMapping("/{studentId}/progress")
    public ApiResponse<DashboardStatsResponse> getAcademicProgress(@PathVariable Integer studentId) {
        return ApiResponse.<DashboardStatsResponse>builder()
                .code(HttpStatus.OK.value())
                .result(academicService.getAcademicProgress(studentId))
                .build();
    }
}
