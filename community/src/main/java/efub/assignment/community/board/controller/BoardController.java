package efub.assignment.community.board.controller;

import efub.assignment.community.board.dto.request.BoardCreateRequest;
import efub.assignment.community.board.dto.request.BoardUpdateRequest;
import efub.assignment.community.board.dto.response.BoardResponse;
import efub.assignment.community.board.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    // 게시판 생성
    @PostMapping
    public ResponseEntity<Void> createBoard(@RequestHeader("Auth-Id") Long memberId,
                                            @Valid @RequestBody BoardCreateRequest request){
        Long id=boardService.createBoard(request, memberId);
        return ResponseEntity.created(URI.create("/boards/"+id)).build();
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponse> getBoard(@PathVariable("boardId") Long id){
        return ResponseEntity.ok(boardService.getBoard(id));
    }

    @PatchMapping("/{boardId}")
    public ResponseEntity<Void> updateBoardOnwer(@PathVariable("boardId") Long boardId,
                                                 @RequestHeader("Auth-Id") Long memberId,
                                                 @RequestHeader("Auth-Password") String password,
                                                 @RequestBody BoardUpdateRequest request){

        boardService.updateBoardOwner(boardId, request, memberId, password);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteBoard(@PathVariable("boardId") Long boardId,
                                            @RequestHeader("Auth-Id") Long memberId,
                                            @RequestHeader("Auth-Password") String password){
        boardService.deleteBoard(boardId, memberId, password);
        return ResponseEntity.noContent().build();
    }
}
