package vn.edu.hcmus.fit.learningpath.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmus.fit.learningpath.dto.response.*;
import vn.edu.hcmus.fit.learningpath.service.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "7. Notifications", description = "System and academic notifications for students")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/{studentId}")
    @Operation(summary = "Get student notifications", description = "Retrieves all notifications assigned to a specific student.")
    public ApiResponse<List<NotificationResponse>> getNotifications(@PathVariable Integer studentId) {
        return ApiResponse.<List<NotificationResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(notificationService.getNotifications(studentId))
                .build();
    }
}
