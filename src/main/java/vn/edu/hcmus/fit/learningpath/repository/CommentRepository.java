package vn.edu.hcmus.fit.learningpath.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmus.fit.learningpath.entity.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Page<Comment> findByPostIdAndParentIsNull(Integer postId, Pageable pageable);
    List<Comment> findByParentId(Integer parentId);
}
