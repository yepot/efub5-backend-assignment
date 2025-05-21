package efub.assignment.community.message.dto.request;

import efub.assignment.community.member.domain.Member;
import efub.assignment.community.message.domain.Message;
import efub.assignment.community.messageRooom.domain.MessageRoom;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MessageCreateRequest(
        @NotNull Long senderId,
        @NotNull Long messageRoomId,
        @NotBlank(message = "내용을 입력해야 합니다.") String messageContent
) {
    public Message toEntity(Member sender, MessageRoom messageRoom){
        return Message.builder()
                .sender(sender)
                .messageRoom(messageRoom)
                .messageContent(messageContent)
                .build();
    }
}
