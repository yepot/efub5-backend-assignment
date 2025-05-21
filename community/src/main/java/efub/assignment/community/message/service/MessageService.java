package efub.assignment.community.message.service;

import efub.assignment.community.member.domain.Member;
import efub.assignment.community.member.service.MemberService;
import efub.assignment.community.message.domain.Message;
import efub.assignment.community.message.dto.request.MessageCreateRequest;
import efub.assignment.community.message.dto.response.MessageInfoResponse;
import efub.assignment.community.message.dto.response.MessageListResponse;
import efub.assignment.community.message.dto.response.MessageResponse;
import efub.assignment.community.message.repository.MessageRepository;
import efub.assignment.community.messageRooom.domain.MessageRoom;
import efub.assignment.community.messageRooom.service.MessageRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final MemberService memberService;
    private final MessageRoomService messageRoomService;

    // 쪽지 생성
    @Transactional
    public MessageResponse createMessage(MessageCreateRequest request){
        Member sender = memberService.findByMemberId(request.senderId());
        MessageRoom messageRoom = messageRoomService.findByMessageRoomId(request.messageRoomId());

        Message message = request.toEntity(sender, messageRoom);
        messageRepository.save(message);

        return MessageResponse.from(message);
    }

    @Transactional(readOnly = true)
    public MessageListResponse getMessages(Long messageRoomId, Long viewerId){
        MessageRoom messageRoom = messageRoomService.findByMessageRoomId(messageRoomId);
        Member viewer = memberService.findByMemberId(viewerId);

        Member opponent = messageRoom.getSender().equals(viewer)? messageRoom.getReceiver():messageRoom.getSender();

        List<Message> messageList = messageRepository.findAllByMessageRoomOrderByCreatedAt(messageRoom);
        List<MessageInfoResponse> messages = messageList.stream()
                .map(msg -> new MessageInfoResponse(
                        msg.getMessageContent(),
                        msg.getCreatedAt(),
                        msg.getSender().equals(viewer)  // 보낸 사람 == 나?
                ))
                .toList();

        return new MessageListResponse(messageRoomId, opponent.getMemberId(), messages);
    }

}


