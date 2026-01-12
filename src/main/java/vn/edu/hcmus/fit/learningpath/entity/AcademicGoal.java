package vn.edu.hcmus.fit.learningpath.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "academic_goals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AcademicGoal {

    @Id
    @Column(name = "student_id")
    private Integer studentId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(name = "total_credits_required")
    private Integer totalCreditsRequired;

    @Column(name = "target_graduation_date")
    private LocalDate targetGraduationDate;

    @Column(name = "note")
    private String note;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
