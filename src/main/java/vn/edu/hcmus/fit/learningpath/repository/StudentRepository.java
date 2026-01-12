package vn.edu.hcmus.fit.learningpath.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmus.fit.learningpath.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {
}
