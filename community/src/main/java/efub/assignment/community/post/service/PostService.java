package efub.assignment.community.post.service;

import efub.assignment.community.board.service.BoardService;
import efub.assignment.community.global.exception.dto.CommunityException;
import efub.assignment.community.global.exception.dto.ExceptionCode;
import efub.assignment.community.member.domain.Member;
import efub.assignment.community.member.repository.MemberRepository;
import efub.assignment.community.member.service.MemberService;
import efub.assignment.community.post.domain.Post;
import efub.assignment.community.post.domain.PostLike;
import efub.assignment.community.post.dto.request.PostCreateRequest;
import efub.assignment.community.post.dto.request.PostUpdateRequest;
import efub.assignment.community.post.dto.response.PostListResponse;
import efub.assignment.community.post.dto.response.PostResponse;
import efub.assignment.community.post.dto.summary.PostSummary;
import efub.assignment.community.post.repository.PostLikeRepository;
import efub.assignment.community.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private BoardService boardService;

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final PostLikeRepository postLikeRepository;

    // 게시글 생성
    @Transactional
    public Long createPost(PostCreateRequest postCreateRequest){
        Long memberId=postCreateRequest.memberId();
        Member writer = memberService.findByMemberId(memberId);
        Post newPost=postCreateRequest.toEntity(writer);
        postRepository.save(newPost);
        return newPost.getId();
    }

    // 게시글 조회
    @Transactional(readOnly = true)
    public PostResponse getPost(Long postId) {
        postRepository.increaseViewCount(postId);
        Post post=findByPostId(postId);
        return PostResponse.from(post);
    }

    @Transactional(readOnly = true)
    public PostListResponse getAllPosts() {
        List<PostSummary> postSummaries=postRepository.findByOrderByCreatedAtDesc().stream()
                .map(PostSummary::from).toList();
        return new PostListResponse(postSummaries, postRepository.count());
    }

    // 게시글 수정
    @Transactional
    public void updatePostContent(Long postId, PostUpdateRequest request, Long memberId, String password) {
        Post post=findByPostId(postId);
        Member member=findByMemberId(memberId);
        authorizePostWriter(post, member, password);
        post.changeContent(request.content());
    }

    //게시글 삭제
    @Transactional
    public void deletePost(Long postId, Long memberId, String password) {
        Post post=findByPostId(postId);
        Member member=findByMemberId(memberId);
        authorizePostWriter(post, member, password);
        postRepository.delete(post);

    }

    @Transactional(readOnly = true)
    public Post findByPostId(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(()->new CommunityException(ExceptionCode.POST_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    private Member findByMemberId(Long memberId) {
        return memberRepository.findByMemberId(memberId)
                .orElseThrow(()->new CommunityException(ExceptionCode.ACCOUNT_NOT_FOUND));
    }

    private void authorizePostWriter(Post post, Member member, String password) {
        if(!post.getWriter().equals(member) || !post.getWriter().getPassword().equals(password)) {
            throw new CommunityException(ExceptionCode.POST_ACCOUNT_MISMATCH);
        }
    }

    // 댓글 좋아요 등록
    @Transactional
    public void likePost(Long postId, Long memberId) {
        Post post=findByPostId(postId);
        Member member=memberService.findByMemberId(memberId);
        if(postLikeRepository.existsByPostAndMember(post, member)){
            throw new CommunityException(ExceptionCode.LIKE_ALREADY_EXISTS);
        }
        PostLike like=PostLike.builder()
                .post(post)
                .member(member)
                .build();
        postLikeRepository.save(like);
    }

    // 게시글 좋아요 삭제
    @Transactional
    public void unlikePost(Long postId, Long memberId) {
        Post post=findByPostId(postId);
        Member member=memberService.findByMemberId(memberId);
        PostLike like=postLikeRepository.findByPostAndMember(post, member)
                .orElseThrow(()->new CommunityException(ExceptionCode.LIKE_NOT_FOUND));
        postLikeRepository.delete(like);
    }
    
}
