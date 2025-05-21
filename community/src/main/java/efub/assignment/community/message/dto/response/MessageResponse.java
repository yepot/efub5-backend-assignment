package efub.assignment.community.message.dto.response;

import efub.assignment.community.message.domain.Message;
import efub.assignment.community.messageRooom.domain.MessageRoom;
import efub.assignment.community.messageRooom.dto.response.MessageRoomResponse;

import java.time.LocalDateTime;

public record MessageResponse(
        Long messageId,
        Long senderId,
        Long messageRoomId,
        String messageContent,
        LocalDateTime createdAt
) {
    public static MessageResponse from(Message message){
        return new MessageResponse(
                message.getMessageId(),
                message.getSender().getMemberId(),
                message.getMessageRoom().getMessageRoomId(),
                message.getMessageContent(),
                message.getCreatedAt()
        );
    }

}