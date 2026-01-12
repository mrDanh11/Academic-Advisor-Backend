package vn.edu.hcmus.fit.learningpath.controller;

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
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/{studentId}")
    public ApiResponse<List<NotificationResponse>> getNotifications(@PathVariable Integer studentId) {
        return ApiResponse.<List<NotificationResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(notificationService.getNotifications(studentId))
                .build();
    }
}
