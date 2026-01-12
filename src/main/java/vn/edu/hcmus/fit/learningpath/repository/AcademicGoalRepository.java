package vn.edu.hcmus.fit.learningpath.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmus.fit.learningpath.entity.AcademicGoal;

import java.util.Optional;

public interface AcademicGoalRepository extends JpaRepository<AcademicGoal, Integer> {
    Optional<AcademicGoal> findByStudentId(Integer studentId);
}
