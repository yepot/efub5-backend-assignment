package efub.assignment.community.post.service;

import efub.assignment.community.board.service.BoardService;
import efub.assignment.community.global.exception.dto.CommunityException;
import efub.assignment.community.global.exception.dto.ExceptionCode;
import efub.assignment.community.member.domain.Member;
import efub.assignment.community.member.repository.MembersRepository;
import efub.assignment.community.member.service.MembersService;
import efub.assignment.community.post.domain.Post;
import efub.assignment.community.post.dto.request.PostCreateRequest;
import efub.assignment.community.post.dto.request.PostUpdateRequest;
import efub.assignment.community.post.dto.response.PostListResponse;
import efub.assignment.community.post.dto.response.PostResponse;
import efub.assignment.community.post.dto.summary.PostSummary;
import efub.assignment.community.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.CommunicationException;
import java.util.List;
import java.util.Optional;

@Service
//@RequiredArgsConstructor
public class PostService {

    private BoardService boardService;

    private final PostRepository postRepository;
    private final MembersRepository membersRepository;
    private final MembersService membersService;

    @Autowired
    public void setBoardService(@Lazy BoardService boardService) {
        this.boardService = boardService;
    }

    public PostService(PostRepository postRepository,
                       MembersRepository membersRepository,
                       MembersService membersService) {
        this.postRepository = postRepository;
        this.membersRepository = membersRepository;
        this.membersService = membersService;
    }

    @Transactional
    public Long createPost(PostCreateRequest postCreateRequest){
        Long memberId=postCreateRequest.memberId();
        Member writer = membersService.findByMemberId(memberId);
        Post newPost=postCreateRequest.toEntity(writer);
        postRepository.save(newPost);
        return newPost.getId();
    }

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

    @Transactional
    public void updatePostContent(Long postId, PostUpdateRequest request, Long memberId, String password) {
        Post post=findByPostId(postId);
        Member member=findByMemberId(memberId);
        authorizePostWriter(post, member, password);
        post.changeContent(request.content());
    }

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
        return membersRepository.findByMemberId(memberId)
                .orElseThrow(()->new CommunityException(ExceptionCode.ACCOUNT_NOT_FOUND));
    }

    private void authorizePostWriter(Post post, Member member, String password) {
        if(!post.getWriter().equals(member) || !post.getWriter().getPassword().equals(password)) {
            throw new CommunityException(ExceptionCode.POST_ACCOUNT_MISMATCH);
        }
    }

}
