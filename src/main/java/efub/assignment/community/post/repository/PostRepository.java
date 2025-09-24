package efub.assignment.community.post.repository;

import efub.assignment.community.board.domain.Board;
import efub.assignment.community.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByPostId(Long postId);

    void deleteByPostId(Long postId);

    List<Post> findAllByBoard(Board board);
}
