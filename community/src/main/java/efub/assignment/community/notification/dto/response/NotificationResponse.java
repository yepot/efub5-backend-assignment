package efub.assignment.community.notification.dto.response;

import java.time.LocalDateTime;

public record NotificationResponse(
        String type,
        String message,
        LocalDateTime notifiedAt
) {
}
