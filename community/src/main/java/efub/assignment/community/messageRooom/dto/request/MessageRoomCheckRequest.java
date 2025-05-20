package efub.assignment.community.messageRooom.dto.request;

import jakarta.validation.constraints.NotNull;

public record MessageRoomCheckRequest (
        @NotNull Long senderId,
        @NotNull Long receiverId,
        @NotNull Long postId
){
}
