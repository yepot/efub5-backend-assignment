package efub.assignment.community.message.repository;

import efub.assignment.community.message.domain.Message;
import efub.assignment.community.messageRooom.domain.MessageRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByMessageRoomOrderByCreatedAt(MessageRoom room);
    Message findTopByMessageRoomOrderByCreatedAtDesc(MessageRoom room);


}
