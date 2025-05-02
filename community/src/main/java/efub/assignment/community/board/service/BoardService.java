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
import efub.assignment.community.member.service.MembersService;
import efub.assignment.community.post.domain.Post;
import efub.assignment.community.post.dto.response.PostResponse;
import efub.assignment.community.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final PostService postService;
    private final BoardRepository boardRepository;
    private final MembersRepository membersRepository;
    private final MembersService membersService;

    @Transactional
    public Long createBoard(BoardCreateRequest boardCreateRequest, Long memberId) throws CommunityException {

        String boardName = boardCreateRequest.boardName();
        String nickname = boardCreateRequest.boardName();

        // 게시판 이름 중복 검사
        if (boardRepository.existsByBoardName(boardName)) {
            throw new CommunityException(ExceptionCode.BOARD_NAME_DUPLICATED);
        }

        if (boardRepository.existsByBoardName(boardName)) {
            throw new CommunityException(ExceptionCode.BOARD_NAME_DUPLICATED);
        }

        // 게시판 주인 조회
        Member owner = membersService.findByMemberId(memberId);

        // 게시판 생성
        Board newBoard = boardCreateRequest.toEntity(owner);
        boardRepository.save(newBoard);

        return newBoard.getBoardId();
    }

    @Transactional(readOnly = true)
    public BoardResponse getBoard(Long boardId) {
        Board board = findByBoardId(boardId);
        return BoardResponse.from(board);
    }

    @Transactional
    public void updateBoardOwner(Long boardId, BoardUpdateRequest request, Long memberId, String password) {
        Board board = findByBoardId(boardId);
        Member member = membersService.findByMemberId(memberId);
        authorizeBoardOwner(board, member, password);
        Member newOwner = membersService.findByMemberId(request.getNewOwnerId());
        board.changeOwner(newOwner);
    }

    @Transactional
    public void deleteBoard(Long boardId, Long memberId, String password){
        Board board=findByBoardId(boardId);
        Member member=membersService.findByMemberId(memberId);
        authorizeBoardOwner(board, member, password);
        boardRepository.delete(board);
    }

    @Transactional(readOnly = true)
    public Board findByBoardId(Long boardId) {
        return boardRepository.findByBoardId(boardId)
                .orElseThrow(()-> new CommunityException(ExceptionCode.POST_NOT_FOUND));
    }

    private void authorizeBoardOwner(Board board, Member member, String password) {
        if(!board.getOwner().equals(member) || !board.getOwner().getPassword().equals(password)) {
            throw new CommunityException(ExceptionCode.POST_ACCOUNT_MISMATCH);
        }
    }

}
