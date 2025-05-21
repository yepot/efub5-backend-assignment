package efub.assignment.community.message.dto.response;

import java.time.LocalDateTime;

public record MessageInfoResponse(
        String messageContent,
        LocalDateTime createdAt,
        boolean isSentByMe
) {
}
