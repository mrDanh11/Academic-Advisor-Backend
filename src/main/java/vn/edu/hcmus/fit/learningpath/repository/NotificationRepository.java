package vn.edu.hcmus.fit.learningpath.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmus.fit.learningpath.entity.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByStudentIdOrderByCreatedAtDesc(Integer studentId);
}
