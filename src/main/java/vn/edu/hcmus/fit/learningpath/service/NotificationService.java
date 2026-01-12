package vn.edu.hcmus.fit.learningpath.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmus.fit.learningpath.dto.response.NotificationResponse;
import vn.edu.hcmus.fit.learningpath.mapper.NotificationMapper;
import vn.edu.hcmus.fit.learningpath.repository.NotificationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotifications(Integer studentId) {
        return notificationRepository.findByStudentIdOrderByCreatedAtDesc(studentId).stream()
                .map(notificationMapper::toResponse)
                .collect(Collectors.toList());
    }
}
