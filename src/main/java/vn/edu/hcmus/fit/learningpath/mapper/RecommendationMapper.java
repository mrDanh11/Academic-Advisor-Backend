package vn.edu.hcmus.fit.learningpath.mapper;

import org.mapstruct.Mapper;
import vn.edu.hcmus.fit.learningpath.entity.AiRecommendation;
import vn.edu.hcmus.fit.learningpath.dto.response.RecommendationResponse;

@Mapper(componentModel = "spring")
public interface RecommendationMapper {
    RecommendationResponse toResponse(AiRecommendation recommendation);
}