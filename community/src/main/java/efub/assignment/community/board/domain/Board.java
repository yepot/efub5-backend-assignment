package efub.assignment.community.board.domain;

import com.fasterxml.jackson.databind.ser.Serializers;
import efub.assignment.community.global.domain.BaseEntity;
import efub.assignment.community.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String boardName;

    private String description;

    private String notice;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member owner;

    @Builder
    public Board(String boardName, String description, String notice, Member owner){
        this.boardName=boardName;
        this.description=description;
        this.notice=notice;
        this.owner=owner;
    }

    public void changeOwner(Member owner){
        this.owner=owner;
    }

}
