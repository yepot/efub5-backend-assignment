package efub.assignment.community.messageRooom.dto.response;

import java.util.List;

public record MessageRoomListResponse(
        List<MessageRoomPreviewResponse> messageRooms
) {
}
