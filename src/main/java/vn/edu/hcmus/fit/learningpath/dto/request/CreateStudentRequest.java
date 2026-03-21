package vn.edu.hcmus.fit.learningpath.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateStudentRequest {
    @NotBlank(message = "Student code is required")
    private String studentCode;

    @NotBlank(message = "Full name is required")
    private String fullName;

    private LocalDate dob;
    private String placeOfBirth;
    private String phone;
    
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;
    
    private String industry;
    private String trainingLevel;
    private String typeOfTraining;
    private String currentPosition;
    private LocalDate startDate;
    
    // Hỗ trợ cả link ảnh có sẵn và upload file trực tiếp
    private String avatarUrl;
    private org.springframework.web.multipart.MultipartFile avatarFile;
}
