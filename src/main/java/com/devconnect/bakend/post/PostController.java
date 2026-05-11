package com.devconnect.bakend.post;


import com.devconnect.bakend.post.dto.CommentRequest;
import com.devconnect.bakend.post.dto.CommentResponse;
import com.devconnect.bakend.post.dto.PostRequest;
import com.devconnect.bakend.post.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest request) {
        return ResponseEntity.ok(postService.createPost(request));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(@RequestBody PostRequest request, @PathVariable Long postId) {
        return ResponseEntity.ok(postService.updatePost(request, postId));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.deletePost(postId));
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<Boolean> toggleLike(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.toggleLikePost(postId));
    }

    @PostMapping("/{postId}/comment")
    public ResponseEntity<CommentResponse> addComment(@RequestBody CommentRequest request, @PathVariable Long postId) {
        return ResponseEntity.ok(postService.addComment(request, postId));
    }

    @DeleteMapping("/{postId}/comment/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        return ResponseEntity.ok(postService.deleteComment(commentId));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPost(postId));
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<Page<CommentResponse>> getComments(@PathVariable Long postId, @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(postService.getPosts(postId, pageable));
    }

    @GetMapping("/feed")
    public ResponseEntity<Page<PostResponse>> getFeed(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(postService.getFeed(pageable));
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<Page<PostResponse>> getUserPosts(@PathVariable String username, @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(postService.getUserPosts(pageable, username));
    }
}