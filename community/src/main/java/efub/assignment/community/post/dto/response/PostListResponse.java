package efub.assignment.community.post.dto.response;

import efub.assignment.community.post.dto.summary.PostSummary;
import lombok.NoArgsConstructor;

import java.util.List;

public record PostListResponse(List<PostSummary> posts, Long totalPosts) {

}