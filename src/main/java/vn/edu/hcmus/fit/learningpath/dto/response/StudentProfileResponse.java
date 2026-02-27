package vn.edu.hcmus.fit.learningpath.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Detailed student profile information")
public class StudentProfileResponse {
    @Schema(description = "Full name of the student", example = "Ly Truong Nam")
    private String fullName;
    
    @Schema(description = "Academic student identifier", example = "22120218")
    private String studentCode;
    
    @Schema(description = "Date of birth", example = "2004-10-23")
    private LocalDate dob;
    
    private String placeOfBirth;
    private String phone;
    
    @Schema(description = "Official university email", example = "22120218@student.hcmus.edu.vn")
    private String email;
    
    @Schema(description = "Field of study", example = "Information Technology")
    private String industry;
    
    private String trainingLevel;
    private String typeOfTraining;
    private String currentPosition;
    private String avatarUrl;
    private LocalDate startDate;
    private String course;
}