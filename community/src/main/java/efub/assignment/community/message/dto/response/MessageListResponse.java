package efub.assignment.community.message.dto.response;

import java.util.List;

public record MessageListResponse(
        Long messageRoomId,
        Long opponentId,
        List<MessageInfoResponse> messages
) {
}
