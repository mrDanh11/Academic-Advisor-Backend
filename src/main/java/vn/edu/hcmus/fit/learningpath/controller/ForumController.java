package vn.edu.hcmus.fit.learningpath.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmus.fit.learningpath.dto.request.CreateCommentRequest;
import vn.edu.hcmus.fit.learningpath.dto.request.CreatePostRequest;
import vn.edu.hcmus.fit.learningpath.dto.request.UpdatePostRequest;
import vn.edu.hcmus.fit.learningpath.dto.response.ApiResponse;
import vn.edu.hcmus.fit.learningpath.dto.response.CommentResponse;
import vn.edu.hcmus.fit.learningpath.dto.response.ForumResponse;
import vn.edu.hcmus.fit.learningpath.dto.response.LikestudentResponse;
import vn.edu.hcmus.fit.learningpath.dto.response.PostResponse;
import vn.edu.hcmus.fit.learningpath.service.ForumService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/forums")
@RequiredArgsConstructor
public class ForumController {
    private final ForumService forumService;

    @GetMapping
    @Tag(name = "1. Categories", description = "Forum categories management")
    @Operation(summary = "Get all accessible forum categories")
    public ApiResponse<List<ForumResponse>> getAllForums(@RequestParam(required = false) Integer studentId) {
        return ApiResponse.success(forumService.getAllForums(studentId));
    }

    @PostMapping("/{forumId}/join")
    @Tag(name = "1. Categories")
    @Operation(summary = "Join a forum (Tham gia diễn đàn)")
    public ApiResponse<String> joinForum(@PathVariable Integer forumId, @RequestParam Integer studentId) {
        return ApiResponse.success(forumService.joinForum(forumId, studentId));
    }

    @PostMapping("/{forumId}/leave")
    @Tag(name = "1. Categories")
    @Operation(summary = "Leave a forum (Rời khỏi diễn đàn)")
    public ApiResponse<String> leaveForum(@PathVariable Integer forumId, @RequestParam Integer studentId) {
        return ApiResponse.success(forumService.leaveForum(forumId, studentId));
    }

    @DeleteMapping("/{forumId}")
    @Tag(name = "1. Categories")
    @Operation(summary = "Delete a forum (Only by owner)")
    public ApiResponse<String> deleteForum(@PathVariable Integer forumId, @RequestParam Integer callerId) {
        forumService.deleteForum(forumId, callerId);
        return ApiResponse.success("Forum deleted successfully");
    }

    @DeleteMapping("/{forumId}/members/{studentId}")
    @Tag(name = "1. Categories")
    @Operation(summary = "Remove a member from forum (Only by owner)")
    public ApiResponse<String> removeMember(
            @PathVariable Integer forumId,
            @PathVariable Integer studentId,
            @RequestParam Integer callerId) {
        return ApiResponse.success(forumService.removeMember(forumId, studentId, callerId));
    }

    @GetMapping("/{forumId}/posts")
    @Tag(name = "2. Posts", description = "Individual posts management")
    @Operation(summary = "Get posts by forum category (Support Private Check)")
    public ApiResponse<Page<PostResponse>> getPostsByForum(
            @PathVariable Integer forumId,
            @RequestParam(required = false) Integer studentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(forumService.getPostsByForum(forumId, studentId, PageRequest.of(page, size, Sort.by("id").descending())));
    }

    @PostMapping("/posts")
    @Tag(name = "2. Posts")
    @Operation(summary = "Create a new post")
    public ApiResponse<PostResponse> createPost(@RequestBody CreatePostRequest request) {
        return ApiResponse.success(forumService.createPost(request));
    }

    @GetMapping("/posts/{postId}")
    @Tag(name = "2. Posts")
    @Operation(summary = "Get post details (Support Private Check)")
    public ApiResponse<PostResponse> getPostById(@PathVariable Integer postId, @RequestParam(required = false) Integer studentId) {
        return ApiResponse.success(forumService.getPostById(postId, studentId));
    }

    @PostMapping("/posts/{postId}/like")
    @Tag(name = "3. Likes & Interactions", description = "Likes and user interactions")
    @Operation(summary = "Toggle Like/Unlike a post (Thích/Bỏ thích bài viết)")
    public ApiResponse<String> toggleLikePost(@PathVariable Integer postId, @RequestParam Integer studentId) {
        return ApiResponse.success(forumService.toggleLikePost(postId, studentId));
    }

    @GetMapping("/posts/{postId}/likes-count")
    @Tag(name = "3. Likes & Interactions")
    @Operation(summary = "Get likes count of a post (Lấy số lượt thích)")
    public ApiResponse<Integer> getPostLikesCount(@PathVariable Integer postId) {
        return ApiResponse.success(forumService.getPostLikesCount(postId));
    }

    @GetMapping("/posts/{postId}/is-liked")
    @Tag(name = "3. Likes & Interactions")
    @Operation(summary = "Check if student liked a post (Kiểm tra sinh viên đã thích bài viết chưa)")
    public ApiResponse<Boolean> checkIfLiked(@PathVariable Integer postId, @RequestParam Integer studentId) {
        return ApiResponse.success(forumService.checkIfStudentLikedPost(postId, studentId));
    }

    @GetMapping("/posts/{postId}/likers")
    @Tag(name = "3. Likes & Interactions")
    @Operation(summary = "Get list of students who liked the post (Xem danh sách người đã thích bài viết)")
    public ApiResponse<List<LikestudentResponse>> getPostLikers(@PathVariable Integer postId) {
        return ApiResponse.success(forumService.getPostLikers(postId));
    }

    @GetMapping("/posts/search")
    @Tag(name = "2. Posts")
    @Operation(summary = "Search posts by keyword (Tìm kiếm bài viết)")
    public ApiResponse<Page<PostResponse>> searchPosts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(forumService.searchPosts(keyword, PageRequest.of(page, size)));
    }

    @GetMapping("/posts/tags/{tag}")
    @Tag(name = "2. Posts")
    @Operation(summary = "Filter posts by tag (Lọc bài viết theo thẻ)")
    public ApiResponse<Page<PostResponse>> getPostsByTag(
            @PathVariable String tag,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(forumService.getPostsByTag(tag, PageRequest.of(page, size)));
    }

    @GetMapping("/posts/trending")
    @Tag(name = "2. Posts")
    @Operation(summary = "Get trending posts (Bài viết nổi bật)")
    public ApiResponse<Page<PostResponse>> getTrendingPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(forumService.getTrendingPosts(PageRequest.of(page, size)));
    }

    @GetMapping("/student/{studentId}/posts")
    @Tag(name = "5. User History", description = "Student's private activity history")
    @Operation(summary = "Get posts by student ID (Bài viết của sinh viên)")
    public ApiResponse<Page<PostResponse>> getPostsByStudent(
            @PathVariable Integer studentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(forumService.getPostsByStudent(studentId, PageRequest.of(page, size, Sort.by("id").descending())));
    }

    @GetMapping("/student/{studentId}/liked-posts")
    @Tag(name = "5. User History")
    @Operation(summary = "Get liked posts by student ID (Bài viết sinh viên đã thích)")
    public ApiResponse<Page<PostResponse>> getLikedPostsByStudent(
            @PathVariable Integer studentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(forumService.getLikedPostsByStudent(studentId, PageRequest.of(page, size)));
    }

    @GetMapping("/posts/{postId}/comments")
    @Tag(name = "4. Comments", description = "Post comments management")
    @Operation(summary = "Get comments for a post")
    public ApiResponse<Page<CommentResponse>> getCommentsByPost(
            @PathVariable Integer postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(forumService.getCommentsByPost(postId, PageRequest.of(page, size)));
    }

    @PostMapping("/comments")
    @Tag(name = "4. Comments")
    @Operation(summary = "Create a new comment or reply")
    public ApiResponse<CommentResponse> createComment(@RequestBody CreateCommentRequest request) {
        return ApiResponse.success(forumService.createComment(request));
    }

    @PutMapping("/posts/{postId}")
    @Tag(name = "2. Posts")
    @Operation(summary = "Update a post (Cập nhật bài viết)")
    public ApiResponse<PostResponse> updatePost(@PathVariable Integer postId, @RequestBody UpdatePostRequest request) {
        return ApiResponse.success(forumService.updatePost(postId, request));
    }

    @DeleteMapping("/posts/{postId}")
    @Tag(name = "2. Posts")
    @Operation(summary = "Delete a post (Support Owner/Author Check)")
    public ApiResponse<String> deletePost(@PathVariable Integer postId, @RequestParam Integer callerId) {
        forumService.deletePostByOwner(postId, callerId);
        return ApiResponse.success("Deleted post successfully");
    }

    @PutMapping("/comments/{commentId}")
    @Tag(name = "4. Comments")
    @Operation(summary = "Update a comment (Cập nhật bình luận)")
    public ApiResponse<CommentResponse> updateComment(@PathVariable Integer commentId, @RequestBody String content) {
        return ApiResponse.success(forumService.updateComment(commentId, content));
    }

    @DeleteMapping("/comments/{commentId}")
    @Tag(name = "4. Comments")
    @Operation(summary = "Delete a comment (Xóa bình luận)")
    public ApiResponse<String> deleteComment(@PathVariable Integer commentId) {
        forumService.deleteComment(commentId);
        return ApiResponse.success("Deleted comment successfully");
    }
}
