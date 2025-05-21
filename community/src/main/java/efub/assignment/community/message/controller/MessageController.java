package efub.assignment.community.message.controller;

import efub.assignment.community.message.dto.request.MessageCreateRequest;
import efub.assignment.community.message.dto.request.MessageListRequest;
import efub.assignment.community.message.dto.response.MessageListResponse;
import efub.assignment.community.message.dto.response.MessageResponse;
import efub.assignment.community.message.service.MessageService;
import efub.assignment.community.messageRooom.dto.request.MessageRoomCreateRequest;
import efub.assignment.community.messageRooom.dto.response.MessageRoomResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class MessageController {

    private final MessageService messageService;

    // 쪽지 생성
    @PostMapping("/messages")
    public ResponseEntity<MessageResponse> createMessage(@Valid @RequestBody MessageCreateRequest request) {
        MessageResponse response = messageService.createMessage(request);
        return ResponseEntity
                .created(URI.create("/messages/" + response.messageId()))
                .body(response);
    }

    // 쪽지 조회 (쪽지방 1개에 있는 모든 쪽지 조회)
    @GetMapping("/message-rooms/{messageRoomId}/messages")
    public ResponseEntity<MessageListResponse> getMessageList(
            @PathVariable("messageRoomId") Long messageRoomId,
            @RequestParam("viewerId") Long viewerId
    ) {
        MessageListRequest request = new MessageListRequest(messageRoomId, viewerId);
        MessageListResponse response = messageService.getMessages(request.messageRoomId(), request.viewerId());
        return ResponseEntity.ok(response);
    }



}

