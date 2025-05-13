package efub.assignment.community.comment.controller;

import efub.assignment.community.comment.dto.request.CommentCreateRequest;
import efub.assignment.community.comment.dto.request.CommentUpdateRequest;
import efub.assignment.community.comment.service.CommentService;
import efub.assignment.community.post.dto.request.PostUpdateRequest;
import efub.assignment.community.post.dto.response.PostCommentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping(("/comments"))
public class CommentController {

    private final CommentService commentService;

    // 댓글 수정
    @PatchMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(@PathVariable("commentId") Long commentId,
                                              @RequestHeader("Auth-Id") Long memberId,
                                              @RequestHeader("Auth-Password") String password,
                                              @RequestBody CommentUpdateRequest commentUpdateRequest) {

        commentService.updateCommentContent(commentId, commentUpdateRequest, memberId, password);
        return ResponseEntity.noContent().build();
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable("commentId") Long commentId,
                                              @RequestHeader("Auth-id") Long memberId,
                                              @RequestHeader("Auth-password") String password) {
        commentService.deleteComment(commentId, memberId, password);
        return ResponseEntity.ok("댓글 삭제 성공");
    }

}
