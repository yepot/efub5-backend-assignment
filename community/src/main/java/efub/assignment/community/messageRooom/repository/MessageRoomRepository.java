package efub.assignment.community.messageRooom.repository;

import efub.assignment.community.member.domain.Member;
import efub.assignment.community.messageRooom.domain.MessageRoom;
import efub.assignment.community.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MessageRoomRepository extends JpaRepository<MessageRoom, Long> {

    Optional<MessageRoom> findBySenderAndReceiverAndPost(Member sender, Member receiver, Post post);
    List<MessageRoom> findAllBySenderAndReceiverAndPost(Member sender, Member receiver, Post post);

    @Query("""
    SELECT r
    FROM MessageRoom r
    WHERE r.sender.memberId = :memberId OR r.receiver.memberId = :memberId
""")
    List<MessageRoom> findAllByParticipant(@Param("memberId") Long memberId);

}

