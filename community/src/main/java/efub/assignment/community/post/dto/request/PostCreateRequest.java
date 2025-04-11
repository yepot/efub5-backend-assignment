package efub.assignment.community.post.dto.request;

import efub.assignment.community.member.domain.Member;
import efub.assignment.community.post.domain.Post;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;

// record : 데이터만 포함하는 불변한 객체를 정의하는 데이터 클래스
public record PostCreateRequest(@NotNull Long memberId, //계정 아이디
                                @NotNull String content,
                                @NotNull boolean anonymous
                                ){
    public Post toEntity(Member member){
        return Post.builder()
                .writer(member)
                .content(content)
                .anonymous(anonymous)
                .build();
    }
}
