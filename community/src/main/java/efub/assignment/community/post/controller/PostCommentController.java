package efub.assignment.community.post.controller;

import efub.assignment.community.comment.dto.request.CommentCreateRequest;
import efub.assignment.community.comment.service.CommentService;
import efub.assignment.community.post.dto.response.PostCommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class PostCommentController {

    private final CommentService commentService;

    // 댓글 생성
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Void> createComment(@PathVariable("postId") Long postId,
                                              @RequestBody CommentCreateRequest request){
        Long commentId = commentService.createComment(postId, request);
        return ResponseEntity.created(URI.create("/posts/"+postId+"/comments/"+commentId)).build();

    }

    // 댓글 조회
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<PostCommentResponse> getComments(@PathVariable("postId") Long postId){
        return ResponseEntity.ok(commentService.getPostCommentList(postId));
    }

    // 댓글 좋아요
    @PostMapping("/comments/{commentId}/like")
    public ResponseEntity<String> likeComment(@PathVariable("commentId") Long commentId,
                                              @RequestHeader("Auth-Id") Long memberId){
        commentService.likeComment(commentId, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).body("댓글 좋아요 성공");
    }

    // 댓글 좋아요 취소
    @DeleteMapping("/comments/{commentId}/like")
    public ResponseEntity<String> unlikeComment(@PathVariable("commentId") Long commentId,
                                                @RequestHeader("Auth-Id") Long memberId) {
        commentService.unlikeComment(commentId, memberId);
        return ResponseEntity.ok("댓글 좋아요 삭제 성공");
    }
}
