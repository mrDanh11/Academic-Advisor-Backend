package vn.edu.hcmus.fit.learningpath.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmus.fit.learningpath.entity.Forum;
import vn.edu.hcmus.fit.learningpath.entity.ForumMembership;
import vn.edu.hcmus.fit.learningpath.entity.Student;

import java.util.Optional;

public interface ForumMembershipRepository extends JpaRepository<ForumMembership, Integer> {
    boolean existsByForum_IdAndStudent_Id(Integer forumId, Integer studentId);
    Optional<ForumMembership> findByForum_IdAndStudent_Id(Integer forumId, Integer studentId);
    void deleteByForum_IdAndStudent_Id(Integer forumId, Integer studentId);
    boolean existsByForum_IdAndStudent_IdAndRole(Integer forumId, Integer studentId, ForumMembership.MembershipRole role);
}
