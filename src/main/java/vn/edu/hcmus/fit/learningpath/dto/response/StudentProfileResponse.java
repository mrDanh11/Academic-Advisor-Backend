package vn.edu.hcmus.fit.learningpath.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentProfileResponse {
    private String fullName;
    private String studentCode;
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
    private String course;
}