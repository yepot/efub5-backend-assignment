package efub.assignment.community.message.dto.request;

public record MessageListRequest(
        Long messageRoomId,
        Long viewerId
) {
}
