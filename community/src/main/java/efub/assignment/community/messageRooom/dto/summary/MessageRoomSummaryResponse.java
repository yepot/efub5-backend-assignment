//package efub.assignment.community.messageRooom.dto.summary;
//
//import efub.assignment.community.messageRooom.domain.MessageRoom;
//import org.apache.logging.log4j.util.PropertySource;
//
//import java.time.LocalDateTime;
//
//public record MessageRoomSummaryResponse(
//        Long messageRoomId,
//        String latestMessage,
//        LocalDateTime latestSentAt
//) {
//    public static MessageRoomSummaryResponse from(MessageRoom room) {
//        Message latest = room.getMessages().stream()
//                .max(PropertySource.Comparator.comparing(Message::getCreatedAt))
//                .orElse(null);
//
//        return new MessageRoomSummaryResponse(
//                room.getMessageRoomId(),
//                latest != null ? latest.getContent() : null,
//                latest != null ? latest.getCreatedAt() : null
//        );
//    }
//}

