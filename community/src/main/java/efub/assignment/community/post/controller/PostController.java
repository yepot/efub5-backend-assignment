package efub.assignment.community.post.controller;

import efub.assignment.community.post.domain.Post;
import efub.assignment.community.post.dto.request.PostCreateRequest;
import efub.assignment.community.post.dto.request.PostUpdateRequest;
import efub.assignment.community.post.dto.response.PostListResponse;
import efub.assignment.community.post.dto.response.PostResponse;
import efub.assignment.community.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    // 게시글 생성
    @PostMapping
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody PostCreateRequest request) {
        Long postId = postService.createPost(request);
        return ResponseEntity.created(URI.create("/posts/" + postId)).build();
    }

    // 게시글 목록 조회
    @GetMapping
    public ResponseEntity<PostListResponse> getAllPost() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    // 게시글 내용 조회
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getpost(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok(postService.getPost(postId));
    }

    // 게시글 내용 수정
    @PatchMapping("/{postId}")
    public ResponseEntity<Void> updatePostContent(@PathVariable("postId") Long postId,
                                                  @RequestHeader("Auth-Id") Long memberId,
                                                  @RequestHeader("Auth-Password") String password,
                                                  @Valid @RequestBody PostUpdateRequest postUpdateRequest) {

        postService.updatePostContent(postId, postUpdateRequest, memberId, password);
        return ResponseEntity.noContent().build();
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable("postId") Long postId,
                                           @RequestHeader("Auth-Id") Long memberId,
                                           @RequestHeader("Auth-Password") String password) {

        postService.deletePost(postId, memberId, password);
        return ResponseEntity.noContent().build();
    }

    // 게시글 좋아요
    @PostMapping("/{postId}/like")

    public ResponseEntity<String> likePost(@PathVariable("postId") Long postId,
                                              @RequestHeader("Auth-Id") Long memberId){
        postService.likePost(postId, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).body("게시글 좋아요 생성 성공");
    }

    // 게시글 좋아요 취소
    @DeleteMapping("/{postId}/like")
    public ResponseEntity<String> unlikePost(@PathVariable("postId") Long postId,
                                                @RequestHeader("Auth-Id") Long memberId) {
        postService.unlikePost(postId, memberId);
        return ResponseEntity.ok("게시글 좋아요 삭제 성공");
    }
}


