package vn.edu.hcmus.fit.learningpath.dto.request;

import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStudentRequest {
    private String fullName;
    private LocalDate dob;
    private String placeOfBirth;
    private String phone;
    
    @Email(message = "Email should be valid")
    private String email;
    
    private String industry;
    private String trainingLevel;
    private String typeOfTraining;
    private String currentPosition;
    private LocalDate startDate;
    
    // Đổi về dùng String avatarUrl thay vì file upload
    private String avatarUrl;
}
