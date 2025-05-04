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
@RequestMapping
public class PostCommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Void> createComment(@PathVariable("postId") Long postId,
                                              @RequestBody CommentCreateRequest request){
        Long commentId = commentService.createComment(postId, request);
        return ResponseEntity.created(URI.create("/posts/"+postId+"/comments/"+commentId)).build();

    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<PostCommentResponse> getComments(@PathVariable("postId") Long postId){
        return ResponseEntity.ok(commentService.getPostCommentList(postId));
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<Void> updateComment(@PathVariable("commentId") Long commentId,
                                              @RequestHeader("Auth-Id") Long memberId,
                                              @RequestHeader("Auth-Password") String password,
                                              @RequestBody CommentUpdateRequest commentUpdateRequest) {

        commentService.updateCommentContent(commentId, commentUpdateRequest, memberId, password);
        return ResponseEntity.noContent().build();
    }

}
