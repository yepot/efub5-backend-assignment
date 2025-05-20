package efub.assignment.community.messageRooom.dto.response;

import efub.assignment.community.messageRooom.domain.MessageRoom;

import java.time.LocalDateTime;

public record MessageRoomResponse(
        Long messageRoomId,
        Long senderId,
        Long receiverId,
        String messageContent,
        LocalDateTime createdAt
) {
    public static MessageRoomResponse from(MessageRoom messageRoom){
        return new MessageRoomResponse(
                messageRoom.getMessageRoomId(),
                messageRoom.getSender().getMemberId(),
                messageRoom.getReceiver().getMemberId(),
                messageRoom.getMessageContent(),
                messageRoom.getCreatedAt()
        );
    }
}


