package vn.edu.hcmus.fit.learningpath.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmus.fit.learningpath.dto.response.*;
import vn.edu.hcmus.fit.learningpath.service.StudentService;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") 
@Tag(name = "9. Students", description = "Student profile and personal information management")
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/{id}/profile")
    @Operation(summary = "Get student profile", description = "Retrieves detailed information of a student including their academic info and contact details.")
    public ApiResponse<StudentProfileResponse> getProfile(@PathVariable Integer id) {
        return ApiResponse.<StudentProfileResponse>builder()
                .code(HttpStatus.OK.value())
                .result(studentService.getProfile(id))
                .build();
    }
}