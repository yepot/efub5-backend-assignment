package efub.assignment.community.board.repository;

import efub.assignment.community.board.domain.Board;
import efub.assignment.community.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    // 게시판 아이디로 게시판 찾기
    Optional<Board> findByBoardId(Long id);

    // 최신순으로 모든 게시글 불러오기
    List<Board> findByOrderByCreatedAtDesc();

}
