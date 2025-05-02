package efub.assignment.community.board.dto.request;

import efub.assignment.community.board.domain.Board;
import efub.assignment.community.member.domain.Member;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.util.Optional;

public record BoardCreateRequest(@NotNull String boardName,
                                 String description,
                                 String notice){

    public Board toEntity(Member member){
        return Board.builder()
                .boardName(boardName)
                .description(description)
                .notice(notice)
                .owner(member)
                .build();
    }



}
