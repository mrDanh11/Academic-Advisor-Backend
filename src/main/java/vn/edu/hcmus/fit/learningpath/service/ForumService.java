package vn.edu.hcmus.fit.learningpath.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmus.fit.learningpath.dto.request.CreateCommentRequest;
import vn.edu.hcmus.fit.learningpath.dto.request.CreateForumRequest;
import vn.edu.hcmus.fit.learningpath.dto.request.CreatePostRequest;
import vn.edu.hcmus.fit.learningpath.dto.request.UpdatePostRequest;
import vn.edu.hcmus.fit.learningpath.dto.response.CommentResponse;
import vn.edu.hcmus.fit.learningpath.dto.response.ForumResponse;
import vn.edu.hcmus.fit.learningpath.dto.response.LikestudentResponse;
import vn.edu.hcmus.fit.learningpath.dto.response.PostResponse;
import vn.edu.hcmus.fit.learningpath.entity.*;
import vn.edu.hcmus.fit.learningpath.exception.AcademicException;
import vn.edu.hcmus.fit.learningpath.repository.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ForumService {
    private final ForumRepository forumRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;
    private final StudentRepository studentRepository;
    private final ForumMembershipRepository forumMembershipRepository;

    @Transactional
    public ForumResponse createForum(CreateForumRequest request) {
        Student author = studentRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new AcademicException(HttpStatus.NOT_FOUND, "Student not found"));

        Forum forum = Forum.builder()
                .name(request.getName())
                .description(request.getDescription())
                .isPublic(request.isPublic())
                .type(request.isPublic() ? Forum.ForumType.GLOBAL : Forum.ForumType.PRIVATE)
                .author(author)
                .membersCount(1) // Author is the first member
                .build();

        Forum savedForum = forumRepository.save(forum);

        // Auto-join the author as OWNER
        ForumMembership membership = ForumMembership.builder()
                .forum(savedForum)
                .student(author)
                .role(ForumMembership.MembershipRole.OWNER)
                .build();
        forumMembershipRepository.save(membership);

        return mapToForumResponse(savedForum);
    }

    public List<ForumResponse> getAllForums(Integer studentId) {
        return forumRepository.findAll().stream()
                .filter(forum -> isVisibleToStudent(forum, studentId))
                .map(this::mapToForumResponse)
                .collect(Collectors.toList());
    }

    private boolean isVisibleToStudent(Forum forum, Integer studentId) {
        if (forum.getType() == Forum.ForumType.GLOBAL || Boolean.TRUE.equals(forum.getIsPublic())) {
            return true;
        }
        if (studentId == null) return false;
        return forumMembershipRepository.existsByForum_IdAndStudent_Id(forum.getId(), studentId);
    }

    public Page<PostResponse> getPostsByForum(Integer forumId, Integer studentId, Pageable pageable) {
        Forum forum = forumRepository.findById(forumId)
                .orElseThrow(() -> new AcademicException(HttpStatus.NOT_FOUND, "Forum not found"));

        if (!isVisibleToStudent(forum, studentId)) {
            throw new AcademicException(HttpStatus.FORBIDDEN, "You do not have permission to view posts in this forum");
        }

        return postRepository.findByForumId(forumId, pageable)
                .map(this::mapToPostResponse);
    }

    @Transactional
    public PostResponse getPostById(Integer postId, Integer studentId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AcademicException(HttpStatus.NOT_FOUND, "Post not found"));

        if (!isVisibleToStudent(post.getForum(), studentId)) {
            throw new AcademicException(HttpStatus.FORBIDDEN, "You do not have permission to view this post");
        }

        post.setViewsCount(post.getViewsCount() + 1);
        return mapToPostResponse(postRepository.save(post));
    }

    @Transactional
    public PostResponse createPost(CreatePostRequest request) {
        Forum forum = forumRepository.findById(request.getForumId())
                .orElseThrow(() -> new AcademicException(HttpStatus.NOT_FOUND, "Forum not found"));
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new AcademicException(HttpStatus.NOT_FOUND, "Student not found"));

        if (forum.getType() == Forum.ForumType.PRIVATE && 
            !forumMembershipRepository.existsByForum_IdAndStudent_Id(forum.getId(), student.getId())) {
            throw new AcademicException(HttpStatus.FORBIDDEN, "You must join this forum before posting");
        }

        Post post = Post.builder()
                .forum(forum)
                .author(student)
                .title(request.getTitle())
                .content(request.getContent())
                .tags(request.getTags())
                .imageUrl(request.getImageUrl())
                .build();
        
        return mapToPostResponse(postRepository.save(post));
    }

    @Transactional
    public PostResponse createPostWithImage(CreatePostRequest request, String imageUrl) {
        Forum forum = forumRepository.findById(request.getForumId())
                .orElseThrow(() -> new AcademicException(HttpStatus.NOT_FOUND, "Forum not found"));
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new AcademicException(HttpStatus.NOT_FOUND, "Student not found"));

        if (forum.getType() == Forum.ForumType.PRIVATE && 
            !forumMembershipRepository.existsByForum_IdAndStudent_Id(forum.getId(), student.getId())) {
            throw new AcademicException(HttpStatus.FORBIDDEN, "You must join this forum before posting");
        }

        Post post = Post.builder()
                .forum(forum)
                .author(student)
                .title(request.getTitle())
                .content(request.getContent())
                .tags(request.getTags())
                .imageUrl(imageUrl)
                .build();
        
        return mapToPostResponse(postRepository.save(post));
    }

    @Transactional
    public String joinForum(Integer forumId, Integer studentId) {
        Forum forum = forumRepository.findById(forumId)
                .orElseThrow(() -> new AcademicException(HttpStatus.NOT_FOUND, "Forum not found"));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new AcademicException(HttpStatus.NOT_FOUND, "Student not found"));

        if (forumMembershipRepository.existsByForum_IdAndStudent_Id(forumId, studentId)) {
            return "Already a member";
        }

        ForumMembership membership = ForumMembership.builder()
                .forum(forum)
                .student(student)
                .role(ForumMembership.MembershipRole.MEMBER)
                .build();
        
        forumMembershipRepository.save(membership);
        forum.setMembersCount(forum.getMembersCount() + 1);
        forumRepository.save(forum);

        return "Joined successfully";
    }

    @Transactional
    public String leaveForum(Integer forumId, Integer studentId) {
        Forum forum = forumRepository.findById(forumId)
                .orElseThrow(() -> new AcademicException(HttpStatus.NOT_FOUND, "Forum not found"));

        if (!forumMembershipRepository.existsByForum_IdAndStudent_Id(forumId, studentId)) {
            return "Not a member";
        }

        forumMembershipRepository.deleteByForum_IdAndStudent_Id(forumId, studentId);
        forum.setMembersCount(Math.max(0, forum.getMembersCount() - 1));
        forumRepository.save(forum);

        return "Left successfully";
    }

    @Transactional
    public void deleteForum(Integer forumId, Integer callerId) {
        Forum forum = forumRepository.findById(forumId)
                .orElseThrow(() -> new AcademicException(HttpStatus.NOT_FOUND, "Forum not found"));

        // Only author/owner can delete
        if (!forum.getAuthor().getId().equals(callerId)) {
            throw new AcademicException(HttpStatus.FORBIDDEN, "Only the author can delete this forum");
        }

        // Deleting forum will cascade delete posts, comments, memberships due to DB constraints
        forumRepository.delete(forum);
    }

    @Transactional
    public String removeMember(Integer forumId, Integer studentId, Integer callerId) {
        Forum forum = forumRepository.findById(forumId)
                .orElseThrow(() -> new AcademicException(HttpStatus.NOT_FOUND, "Forum not found"));

        // Only owner can remove members
        boolean isOwner = forumMembershipRepository.existsByForum_IdAndStudent_IdAndRole(
                forumId, callerId, ForumMembership.MembershipRole.OWNER);
        
        if (!isOwner && !forum.getAuthor().getId().equals(callerId)) {
            throw new AcademicException(HttpStatus.FORBIDDEN, "Only the owner can remove members");
        }

        if (studentId.equals(callerId)) {
            throw new AcademicException(HttpStatus.BAD_REQUEST, "Owner cannot remove themselves. Use delete forum instead.");
        }

        if (forumMembershipRepository.existsByForum_IdAndStudent_Id(forumId, studentId)) {
            forumMembershipRepository.deleteByForum_IdAndStudent_Id(forumId, studentId);
            forum.setMembersCount(Math.max(0, forum.getMembersCount() - 1));
            forumRepository.save(forum);
            return "Member removed successfully";
        }

        return "Student is not a member";
    }

    @Transactional
    public void deletePostByOwner(Integer postId, Integer callerId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AcademicException(HttpStatus.NOT_FOUND, "Post not found"));

        // Allow if caller is post author OR forum owner
        boolean isForumOwner = forumMembershipRepository.existsByForum_IdAndStudent_IdAndRole(
                post.getForum().getId(), callerId, ForumMembership.MembershipRole.OWNER);

        if (!post.getAuthor().getId().equals(callerId) && !isForumOwner) {
            throw new AcademicException(HttpStatus.FORBIDDEN, "You don't have permission to delete this post");
        }

        postRepository.delete(post);
    }

    private ForumResponse mapToForumResponse(Forum forum) {
        return ForumResponse.builder()
                .id(forum.getId())
                .name(forum.getName())
                .description(forum.getDescription())
                .rules(forum.getRules())
                .isPublic(forum.getIsPublic())
                .type(forum.getType().name())
                .authorId(forum.getAuthor() != null ? forum.getAuthor().getId() : null)
                .authorName(forum.getAuthor() != null ? forum.getAuthor().getFullName() : "Admin")
                .authorAvatarUrl(forum.getAuthor() != null ? forum.getAuthor().getAvatarUrl() : null)
                .membersCount(forum.getMembersCount())
                .avatarUrl(forum.getAvatarUrl())
                .createdAt(forum.getCreatedAt())
                .build();
    }

    @Transactional
    public String toggleLikePost(Integer postId, Integer studentId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AcademicException(HttpStatus.NOT_FOUND, "Post not found"));
        
        if (postLikeRepository.existsByPostIdAndStudentId(postId, studentId)) {
            postLikeRepository.deleteByPostIdAndStudentId(postId, studentId);
            post.setLikesCount(Math.max(0, post.getLikesCount() - 1));
            postRepository.save(post);
            return "Unliked successfully";
        } else {
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new AcademicException(HttpStatus.NOT_FOUND, "Student not found"));
            
            PostLike like = PostLike.builder()
                    .id(new PostLike.PostLikeId(postId, studentId))
                    .post(post)
                    .student(student)
                    .build();
            postLikeRepository.save(like);
            
            post.setLikesCount(post.getLikesCount() + 1);
            postRepository.save(post);
            return "Liked successfully";
        }
    }

    public Integer getPostLikesCount(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AcademicException(HttpStatus.NOT_FOUND, "Post not found"));
        return post.getLikesCount();
    }

    public boolean checkIfStudentLikedPost(Integer postId, Integer studentId) {
        return postLikeRepository.existsByPostIdAndStudentId(postId, studentId);
    }

    @Transactional
    public Page<CommentResponse> getCommentsByPost(Integer postId, Pageable pageable) {
        return commentRepository.findByPostIdAndParentIsNull(postId, pageable)
                .map(this::mapWithReplies);
    }

    @Transactional
    public CommentResponse createComment(CreateCommentRequest request) {
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new AcademicException(HttpStatus.NOT_FOUND, "Post not found"));
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new AcademicException(HttpStatus.NOT_FOUND, "Student not found"));
        
        Comment parent = null;
        if (request.getParentId() != null) {
            parent = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new AcademicException(HttpStatus.NOT_FOUND, "Parent comment not found"));
        }

        Comment comment = Comment.builder()
                .post(post)
                .author(student)
                .content(request.getContent())
                .parent(parent)
                .build();
        
        comment = commentRepository.save(comment);

        post.setCommentsCount(post.getCommentsCount() + 1);
        postRepository.save(post);

        return mapToCommentResponse(comment);
    }

    public List<LikestudentResponse> getPostLikers(Integer postId) {
        if (!postRepository.existsById(postId)) {
            throw new AcademicException(HttpStatus.NOT_FOUND, "Post not found");
        }
        return postLikeRepository.findByPostId(postId).stream()
                .map(like -> LikestudentResponse.builder()
                        .id(like.getStudent().getId())
                        .fullName(like.getStudent().getFullName())
                        .studentCode(like.getStudent().getStudentCode())
                        .avatarUrl(like.getStudent().getAvatarUrl())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public PostResponse updatePost(Integer postId, UpdatePostRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AcademicException(HttpStatus.NOT_FOUND, "Post not found"));
        
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setTags(request.getTags());
        
        return mapToPostResponse(postRepository.save(post));
    }

    @Transactional
    public CommentResponse updateComment(Integer commentId, String content) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AcademicException(HttpStatus.NOT_FOUND, "Comment not found"));
        
        comment.setContent(content);
        return mapToCommentResponse(commentRepository.save(comment));
    }

    @Transactional
    public void deleteComment(Integer commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AcademicException(HttpStatus.NOT_FOUND, "Comment not found"));
        
        Post post = comment.getPost();
        
        // Count comments to delete (including nested replies)
        long countToDelete = countCommentAndReplies(comment);
        
        commentRepository.delete(comment);

        // Update comment count for the post
        post.setCommentsCount(Math.max(0, post.getCommentsCount() - (int)countToDelete));
        postRepository.save(post);
    }

    public Page<PostResponse> searchPosts(String keyword, Pageable pageable) {
        return postRepository.searchPosts(keyword, pageable).map(this::mapToPostResponse);
    }

    public Page<PostResponse> getPostsByTag(String tag, Pageable pageable) {
        return postRepository.findByTag(tag, pageable).map(this::mapToPostResponse);
    }

    public Page<PostResponse> getTrendingPosts(Pageable pageable) {
        return postRepository.findTrendingPosts(pageable).map(this::mapToPostResponse);
    }

    public Page<PostResponse> getPostsByStudent(Integer studentId, Pageable pageable) {
        return postRepository.findByAuthorId(studentId, pageable).map(this::mapToPostResponse);
    }

    public Page<PostResponse> getLikedPostsByStudent(Integer studentId, Pageable pageable) {
        return postRepository.findLikedPostsByStudentId(studentId, pageable).map(this::mapToPostResponse);
    }

    private long countCommentAndReplies(Comment comment) {
        List<Comment> replies = commentRepository.findByParentId(comment.getId());
        long count = 1; // current comment
        for (Comment reply : replies) {
            count += countCommentAndReplies(reply);
        }
        return count;
    }

    private PostResponse mapToPostResponse(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .forumId(post.getForum().getId())
                .authorId(post.getAuthor().getId())
                .authorName(post.getAuthor().getFullName())
                .authorAvatarUrl(post.getAuthor().getAvatarUrl())
                .title(post.getTitle())
                .content(post.getContent())
                .imageUrl(post.getImageUrl())
                .tags(post.getTags())
                .viewsCount(post.getViewsCount())
                .likesCount(post.getLikesCount())
                .commentsCount(post.getCommentsCount())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    private CommentResponse mapToCommentResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .authorId(comment.getAuthor().getId())
                .authorName(comment.getAuthor().getFullName())
                .authorAvatarUrl(comment.getAuthor().getAvatarUrl())
                .content(comment.getContent())
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .createdAt(comment.getCreatedAt())
                .build();
    }

    private CommentResponse mapWithReplies(Comment comment) {
        CommentResponse response = mapToCommentResponse(comment);
        List<CommentResponse> replies = commentRepository.findByParentId(comment.getId())
                .stream()
                .map(this::mapWithReplies)
                .collect(Collectors.toList());
        response.setReplies(replies);
        return response;
    }
}
