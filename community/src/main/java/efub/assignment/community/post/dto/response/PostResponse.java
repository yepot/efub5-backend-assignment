package efub.assignment.community.post.dto.response;

import efub.assignment.community.message.domain.Message;
import efub.assignment.community.post.domain.Post;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PostResponse(Long postId,
                           String nickname,
                           boolean anonymous,
                           String content,
                           Long viewCount,
                           LocalDateTime createdAt,
                           LocalDateTime modifiedAt) {

    public static PostResponse from(Post post){
        return new PostResponse(
                post.getId(),
                post.getWriter().getNickname(),
                post.isAnonymous(),
                post.getContent(),
                post.getViewCount(),
                post.getCreatedAt(),
                post.getModifiedAt()
        );
    }
}

