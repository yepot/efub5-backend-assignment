package efub.assignment.community.messageRooom.service;

import efub.assignment.community.global.exception.dto.CommunityException;
import efub.assignment.community.global.exception.dto.ExceptionCode;
import efub.assignment.community.member.domain.Member;
import efub.assignment.community.member.repository.MemberRepository;
import efub.assignment.community.member.service.MemberService;
import efub.assignment.community.message.domain.Message;
import efub.assignment.community.message.repository.MessageRepository;
import efub.assignment.community.messageRooom.domain.MessageRoom;
import efub.assignment.community.messageRooom.dto.request.MessageRoomCheckRequest;
import efub.assignment.community.messageRooom.dto.request.MessageRoomCreateRequest;
import efub.assignment.community.messageRooom.dto.response.MessageRoomListResponse;
import efub.assignment.community.messageRooom.dto.response.MessageRoomPreviewResponse;
import efub.assignment.community.messageRooom.dto.response.MessageRoomResponse;
import efub.assignment.community.messageRooom.repository.MessageRoomRepository;
import efub.assignment.community.notification.domain.Notification;
import efub.assignment.community.notification.domain.NotificationType;
import efub.assignment.community.notification.repository.NotificationRepository;
import efub.assignment.community.post.domain.Post;
import efub.assignment.community.post.repository.PostRepository;
import efub.assignment.community.post.service.PostService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MessageRoomService {

    private final MessageRepository messageRepository;
    private final MessageRoomRepository messageRoomRepository;
    private final NotificationRepository notificationRepository;
    private final MemberService memberService;
    private final PostService postService;

    @Transactional
    public MessageRoomResponse createMessageRoom(MessageRoomCreateRequest request) {
        Member sender = memberService.findByMemberId(request.senderId());
        Member receiver = memberService.findByMemberId(request.receiverId());
        Post post = postService.findByPostId(request.postId());

        MessageRoom messageRoom = request.toEntity(sender, receiver, post);
        messageRoomRepository.save(messageRoom);

        // 쪽지방 알림 생성
        Notification notification = Notification.builder()
                .type(NotificationType.MESSAGE_ROOM)
                .referenceId(messageRoom.getMessageRoomId())
                .member(receiver)
                .notifiedAt(LocalDateTime.now())
                .build();
        notificationRepository.save(notification);

        return MessageRoomResponse.from(messageRoom);
    }

    @Transactional
    public MessageRoomResponse findExistingMessageRoom(MessageRoomCheckRequest request) {
        Member sender = memberService.findByMemberId(request.senderId());
        Member receiver = memberService.findByMemberId(request.receiverId());
        Post post = postService.findByPostId(request.postId());

        MessageRoom messageRoom = messageRoomRepository
                .findBySenderAndReceiverAndPost(sender, receiver, post)
                .orElseThrow(() -> new EntityNotFoundException("메시지방이 존재하지 않습니다."));

        return MessageRoomResponse.from(messageRoom);
    }

    @Transactional
    public Long findExistingMessageRoomId(MessageRoomCheckRequest request) {
        Member sender = memberService.findByMemberId(request.senderId());
        Member receiver = memberService.findByMemberId(request.receiverId());
        Post post = postService.findByPostId(request.postId());

        List<MessageRoom> rooms = messageRoomRepository.findAllBySenderAndReceiverAndPost(sender, receiver, post);

        if (rooms.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, "메시지방이 존재하지 않습니다.");
        }

        return rooms.get(0).getMessageRoomId();  // 가장 첫 번째 결과만 사용
    }

    @Transactional
    public void deleteMessageRoom(Long messageRoomId) {
        MessageRoom messageRoom = messageRoomRepository.findById(messageRoomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "쪽지방이 존재하지 않습니다."));

        messageRoomRepository.delete(messageRoom);
    }

    @Transactional
    public MessageRoom findByMessageRoomId(Long messageRoomId) {
        return messageRoomRepository.findById(messageRoomId)
                .orElseThrow(() -> new EntityNotFoundException("해당 메시지방이 존재하지 않습니다."));
    }

    @Transactional
    public MessageRoomListResponse getMessageRoomsByMember(Long memberId) {
        Member member = memberService.findByMemberId(memberId);

        List<MessageRoom> messageRooms = messageRoomRepository.findAllByParticipant(memberId);

        List<MessageRoomPreviewResponse> roomPreviews = messageRooms.stream()
                .map(messageRoom -> {
                    Message latest = messageRepository.findTopByMessageRoomOrderByCreatedAtDesc(messageRoom);
                    return new MessageRoomPreviewResponse(
                            messageRoom.getMessageRoomId(),
                            latest != null ? latest.getMessageContent() : "(메시지 없음)",
                            latest != null ? latest.getCreatedAt() : null
                    );
                })
                .toList();

        return new MessageRoomListResponse(roomPreviews);
    }


}
