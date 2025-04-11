package efub.assignment.community.post.dto.summary;

import efub.assignment.community.post.domain.Post;

public record PostSummary(Long postId, Long viewCount) {
    public static PostSummary from(Post post){
        return  new PostSummary(
                post.getId(),
                post.getViewCount()
        );
    }
}
