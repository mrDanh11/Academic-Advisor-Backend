package vn.edu.hcmus.fit.learningpath.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.hcmus.fit.learningpath.entity.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Page<Post> findByForumId(Integer forumId, Pageable pageable);
    Page<Post> findByAuthorId(Integer studentId, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Post> searchPosts(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE LOWER(p.tags) LIKE LOWER(CONCAT('%', :tag, '%'))")
    Page<Post> findByTag(@Param("tag") String tag, Pageable pageable);

    @Query("SELECT p FROM Post p ORDER BY p.viewsCount DESC, p.likesCount DESC")
    Page<Post> findTrendingPosts(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.forum.type = 'GLOBAL' OR " +
           "( :studentId IS NOT NULL AND EXISTS (" +
           "  SELECT 1 FROM ForumMembership fm " +
           "  WHERE fm.student.id = :studentId AND fm.forum.id = p.forum.id))")
    Page<Post> findFeedPosts(@Param("studentId") Integer studentId, Pageable pageable);

    @Query("SELECT pl.post FROM PostLike pl WHERE pl.student.id = :studentId")
    Page<Post> findLikedPostsByStudentId(@Param("studentId") Integer studentId, Pageable pageable);
}
