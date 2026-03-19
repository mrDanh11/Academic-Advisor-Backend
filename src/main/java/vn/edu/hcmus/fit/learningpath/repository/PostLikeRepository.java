package vn.edu.hcmus.fit.learningpath.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmus.fit.learningpath.entity.PostLike;
import java.util.List;

public interface PostLikeRepository extends JpaRepository<PostLike, PostLike.PostLikeId> {
    boolean existsByPostIdAndStudentId(Integer postId, Integer studentId);
    void deleteByPostIdAndStudentId(Integer postId, Integer studentId);
    List<PostLike> findByPostId(Integer postId);
}
