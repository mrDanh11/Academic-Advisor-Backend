package vn.edu.hcmus.fit.learningpath.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmus.fit.learningpath.entity.AiRecommendation;

import java.util.List;

public interface AiRecommendationRepository extends JpaRepository<AiRecommendation, Integer> {
    List<AiRecommendation> findByStudentIdOrderByCreatedAtDesc(Integer studentId);
}
