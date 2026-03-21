package vn.edu.hcmus.fit.learningpath.dto.response;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponse {
    private Integer id;
    private String studentCode;
    private String fullName;
    private LocalDate dob;
    private String placeOfBirth;
    private String phone;
    private String email;
    private String industry;
    private String trainingLevel;
    private String typeOfTraining;
    private String currentPosition;
    private String avatarUrl;
    private LocalDate startDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
