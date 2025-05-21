package efub.assignment.community.messageRooom.service;

import efub.assignment.community.global.exception.dto.CommunityException;
import efub.assignment.community.global.exception.dto.ExceptionCode;
import efub.assignment.community.member.domain.Member;
import efub.assignment.community.member.repository.MemberRepository;
import efub.assignment.community.member.service.MemberService;
import efub.assignment.community.messageRooom.domain.MessageRoom;
import efub.assignment.community.messageRooom.dto.request.MessageRoomCheckRequest;
import efub.assignment.community.messageRooom.dto.request.MessageRoomCreateRequest;
import efub.assignment.community.messageRooom.dto.response.MessageRoomResponse;
import efub.assignment.community.messageRooom.repository.MessageRoomRepository;
import efub.assignment.community.post.domain.Post;
import efub.assignment.community.post.repository.PostRepository;
import efub.assignment.community.post.service.PostService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MessageRoomService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final MessageRoomRepository messageRoomRepository;
    private final MemberService memberService;
    private final PostService postService;

    @Transactional
    public MessageRoomResponse createMessageRoom(MessageRoomCreateRequest request) {
        Member sender = memberService.findByMemberId(request.senderId());
        Member receiver = memberService.findByMemberId(request.receiverId());
        Post post = postService.findByPostId(request.postId());

        MessageRoom messageRoom = request.toEntity(sender, receiver, post);
        messageRoomRepository.save(messageRoom);

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



}
