package vn.edu.hcmus.fit.learningpath.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "student_code", length = 20, nullable = false, unique = true)
    private String studentCode;

    @Column(name = "full_name", length = 100, nullable = false)
    private String fullName;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "place_of_birth", length = 255)
    private String placeOfBirth;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "email", length = 100, unique = true)
    private String email;

    @Column(name = "industry", length = 100)
    private String industry;

    @Column(name = "training_level", length = 50)
    private String trainingLevel;

    @Column(name = "type_of_training", length = 50)
    private String typeOfTraining;

    @Column(name = "current_position", length = 100)
    private String currentPosition;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "start_date")
    private LocalDate startDate;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentCourse> studentCourses = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AiRecommendation> aiRecommendations = new ArrayList<>();

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private AcademicGoal academicGoal;
}
