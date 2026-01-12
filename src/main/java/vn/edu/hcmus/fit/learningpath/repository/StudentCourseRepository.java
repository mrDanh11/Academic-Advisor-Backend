package vn.edu.hcmus.fit.learningpath.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmus.fit.learningpath.entity.StudentCourse;

import java.util.List;

public interface StudentCourseRepository extends JpaRepository<StudentCourse, Integer> {
    List<StudentCourse> findByStudentId(Integer studentId);
    
    List<StudentCourse> findByStudentIdAndStatus(Integer studentId, String status);
}
