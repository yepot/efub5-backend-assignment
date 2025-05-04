package efub.assignment.community.post.domain;

import efub.assignment.community.comment.domain.Comment;
import efub.assignment.community.global.domain.BaseEntity;
import efub.assignment.community.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class Post extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 내용
    private String content;

    // 글쓴이
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member writer;

    // 조회수
    private Long viewCount;

    // 익명 여부
    private boolean anonymous;

    // 빌더
    @Builder
    public Post(Member writer, String content, boolean anonymous){
        this.writer=writer;
        this.content=content;
        this.anonymous=anonymous;
        this.viewCount=0L;
    }

    // 게시물 내용 수정
    public void changeContent(String newContent){
        this.content=newContent;
    }

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList=new ArrayList<>();

}
