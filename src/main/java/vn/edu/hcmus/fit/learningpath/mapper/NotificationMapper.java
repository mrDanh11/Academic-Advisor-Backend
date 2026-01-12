package vn.edu.hcmus.fit.learningpath.mapper;

import org.mapstruct.Mapper;
import vn.edu.hcmus.fit.learningpath.entity.Notification;
import vn.edu.hcmus.fit.learningpath.dto.response.NotificationResponse;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationResponse toResponse(Notification notification);
}