package efub.assignment.community.post.repository;

import efub.assignment.community.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 게시물 아이디로 게시물 찾기
    Optional<Post> findById(Long id);

    // 최신순으로 모든 게시글 불러오기
    List<Post> findByOrderByCreatedAtDesc();

    // 조회수 상승
    @Modifying(clearAutomatically = true)
    @Query("Update Post p SET p.viewCount = p.viewCount + 1 where p.id = :postId")
    void increaseViewCount(@Param("postId") Long postId);
}
