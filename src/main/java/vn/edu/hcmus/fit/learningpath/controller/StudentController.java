package vn.edu.hcmus.fit.learningpath.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmus.fit.learningpath.dto.request.CreateStudentRequest;
import vn.edu.hcmus.fit.learningpath.dto.request.UpdateStudentRequest;
import vn.edu.hcmus.fit.learningpath.dto.response.*;
import vn.edu.hcmus.fit.learningpath.service.StudentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") 
@Tag(name = "9. Students", description = "Student profile and personal information management")
public class StudentController {

    private final StudentService studentService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create a new student with avatar upload")
    public ApiResponse<StudentResponse> createStudent(
            @RequestPart("data") String dataJson,
            @RequestPart(value = "avatar", required = false) org.springframework.web.multipart.MultipartFile avatar) {
        
        CreateStudentRequest request;
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
            request = mapper.readValue(dataJson, CreateStudentRequest.class);
        } catch (Exception e) {
            throw new vn.edu.hcmus.fit.learningpath.exception.AcademicException(HttpStatus.BAD_REQUEST, "Invalid JSON data format in 'data' part");
        }

        if (avatar != null) {
            request.setAvatarFile(avatar);
        }
        return ApiResponse.<StudentResponse>builder()
                .code(HttpStatus.CREATED.value())
                .result(studentService.createStudent(request))
                .build();
    }

    @GetMapping("/{id}/profile")
    @Operation(summary = "Get student profile by ID")
    public ApiResponse<StudentResponse> getStudentProfile(@PathVariable Integer id) {
        return ApiResponse.<StudentResponse>builder()
                .code(HttpStatus.OK.value())
                .result(studentService.getStudentById(id))
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update student information")
    public ApiResponse<StudentResponse> updateStudent(@PathVariable Integer id, @RequestBody UpdateStudentRequest request) {
        return ApiResponse.<StudentResponse>builder()
                .code(HttpStatus.OK.value())
                .result(studentService.updateStudent(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete student profile")
    public ApiResponse<String> deleteStudent(@PathVariable Integer id) {
        studentService.deleteStudent(id);
        return ApiResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .message("Student deleted successfully")
                .build();
    }

    @PostMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload student avatar (Supabase)")
    public ApiResponse<StudentResponse> uploadAvatar(
            @PathVariable Integer id,
            @RequestPart("file") org.springframework.web.multipart.MultipartFile file) {
        return ApiResponse.<StudentResponse>builder()
                .code(HttpStatus.OK.value())
                .result(studentService.uploadAvatar(id, file))
                .build();
    }
}