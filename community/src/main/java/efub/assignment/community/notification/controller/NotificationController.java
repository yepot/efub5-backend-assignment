package efub.assignment.community.notification.controller;

import efub.assignment.community.notification.dto.response.NotificationResponse;
import efub.assignment.community.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/{memberId}")
    public ResponseEntity<List<NotificationResponse>> getNotifications(@PathVariable("memberId") Long memberId) {
        return ResponseEntity.ok(notificationService.getNotifications(memberId));
    }

}
