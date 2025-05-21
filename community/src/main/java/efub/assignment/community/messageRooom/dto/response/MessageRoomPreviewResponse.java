package efub.assignment.community.messageRooom.dto.response;

import java.time.LocalDateTime;

public record MessageRoomPreviewResponse(
        Long memberId,
        String latestMessage,
        LocalDateTime latestCreatedAt
) {
}
