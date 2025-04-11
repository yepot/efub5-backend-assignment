package efub.assignment.community.board.service;

import efub.assignment.community.board.domain.Board;
import efub.assignment.community.board.dto.request.BoardCreateRequest;
import efub.assignment.community.board.dto.request.BoardUpdateRequest;
import efub.assignment.community.board.dto.response.BoardResponse;
import efub.assignment.community.board.repository.BoardRepository;
import efub.assignment.community.global.exception.dto.ClientExceptionCode;
import efub.assignment.community.global.exception.dto.CommunityException;
import efub.assignment.community.global.exception.dto.ExceptionCode;
import efub.assignment.community.member.domain.Member;
import efub.assignment.community.member.repository.MembersRepository;
import efub.assignment.community.post.domain.Post;
import efub.assignment.community.post.dto.response.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MembersRepository membersRepository;

    @Transactional
    public Long createBoard(BoardCreateRequest boardCreateRequest) {

        String boardName = boardCreateRequest.boardName();
        String nickname = boardCreateRequest.boardName();

//        // 게시판 이름 중복 검사
//        if (boardRepository.existsByBoardName(boardName)) {
//            throw new CommunityException(ClientExceptionCode.BOARD_NAME_DUPLICATED, "이미 존재하는 게시판 이름입니다.");
//        }


        // 게시판 주인 조회
        Member owner = membersRepository.findByNickname(nickname);
        //.orElseThrow(() -> new CommunityException(ClientExceptionCode.ACCOUNT_NOT_FOUND, "존재하지 않는 회원입니다. nickname=" + nickname));

        // 게시판 생성
        Board newBoard = boardCreateRequest.toEntity(owner);
        boardRepository.save(newBoard);

        return newBoard.getId();
    }

    @Transactional
    public BoardResponse getBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CommunityException(ClientExceptionCode.BOARD_NOT_FOUND));
        return BoardResponse.from(board);
    }

//    @Transactional
//    public void updateBoardOwner(Long boardId, BoardUpdateRequest request, Long memberId, String password) {
//
//    }
//
//    @Transactional
//    public void deleteBoard(Long boardId, Long memberId, String password){
//        Board board=findByBoardId(boardId);
//        Member member=findByMemberId(memberId);
//        boardRepository.delete(board);
//    }


}
