package efub.assignment.community.comment.dto.response;

import efub.assignment.community.comment.domain.Comment;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentResponse {

    private final Long commentId;
    private final Long postId;
    private final String writerNickname;
    private final boolean anonymous;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static CommentResponse of(Comment comment){
        return CommentResponse.builder()
                .commentId(comment.getCommentId())
                .postId(comment.getPost().getId())
                .writerNickname(comment.getWriter().getNickname())
                .anonymous(comment.isAnonymous())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getModifiedAt())
                .build();
    }
}
