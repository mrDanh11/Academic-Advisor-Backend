package vn.edu.hcmus.fit.learningpath.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmus.fit.learningpath.dto.request.CreateCommentRequest;
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

    public List<ForumResponse> getAllForums() {
        return forumRepository.findAll().stream()
                .map(this::mapToForumResponse)
                .collect(Collectors.toList());
    }

    public Page<PostResponse> getPostsByForum(Integer forumId, Pageable pageable) {
        return postRepository.findByForumId(forumId, pageable)
                .map(this::mapToPostResponse);
    }

    @Transactional
    public PostResponse getPostById(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AcademicException(HttpStatus.NOT_FOUND, "Post not found"));
        
        post.setViewsCount(post.getViewsCount() + 1);
        return mapToPostResponse(postRepository.save(post));
    }

    @Transactional
    public PostResponse createPost(CreatePostRequest request) {
        Forum forum = forumRepository.findById(request.getForumId())
                .orElseThrow(() -> new AcademicException(HttpStatus.NOT_FOUND, "Forum not found"));
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new AcademicException(HttpStatus.NOT_FOUND, "Student not found"));

        Post post = Post.builder()
                .forum(forum)
                .author(student)
                .title(request.getTitle())
                .content(request.getContent())
                .tags(request.getTags())
                .build();
        
        return mapToPostResponse(postRepository.save(post));
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
    public void deletePost(Integer postId) {
        if (!postRepository.existsById(postId)) {
            throw new AcademicException(HttpStatus.NOT_FOUND, "Post not found");
        }
        postRepository.deleteById(postId);
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

    private ForumResponse mapToForumResponse(Forum forum) {
        return ForumResponse.builder()
                .id(forum.getId())
                .name(forum.getName())
                .description(forum.getDescription())
                .rules(forum.getRules())
                .isPublic(forum.getIsPublic())
                .createdAt(forum.getCreatedAt())
                .build();
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
