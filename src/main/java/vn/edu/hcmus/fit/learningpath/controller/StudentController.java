package vn.edu.hcmus.fit.learningpath.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmus.fit.learningpath.dto.response.*;
import vn.edu.hcmus.fit.learningpath.service.StudentService;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") 
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/{id}/profile")
    public ApiResponse<StudentProfileResponse> getProfile(@PathVariable Integer id) {
        return ApiResponse.<StudentProfileResponse>builder()
                .code(HttpStatus.OK.value())
                .result(studentService.getProfile(id))
                .build();
    }
}