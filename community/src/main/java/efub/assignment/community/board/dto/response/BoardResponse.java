package efub.assignment.community.board.dto.response;

import efub.assignment.community.board.domain.Board;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record BoardResponse(Long boardId,
                            String boardName,
                            String description,
                            String notice,
                            String owner,
                            LocalDateTime createdAt,
                            LocalDateTime modifiedAt) {

    public static BoardResponse from(Board board){
        return new BoardResponse(
                board.getId(),
                board.getBoardName(),
                board.getDescription(),
                board.getNotice(),
                board.getOwner().getNickname(),
                board.getCreatedAt(),
                board.getModifiedAt()
        );
    }
}
