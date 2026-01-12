package vn.edu.hcmus.fit.learningpath.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmus.fit.learningpath.dto.response.RecommendationResponse;
import vn.edu.hcmus.fit.learningpath.mapper.RecommendationMapper;
import vn.edu.hcmus.fit.learningpath.repository.AiRecommendationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final AiRecommendationRepository aiRecommendationRepository;
    private final RecommendationMapper recommendationMapper;

    @Transactional(readOnly = true)
    public List<RecommendationResponse> getRecommendations(Integer studentId) {
        return aiRecommendationRepository.findByStudentIdOrderByCreatedAtDesc(studentId).stream()
                .map(recommendationMapper::toResponse)
                .collect(Collectors.toList());
    }
}
