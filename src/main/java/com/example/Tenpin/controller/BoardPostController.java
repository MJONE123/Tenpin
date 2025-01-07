package com.example.Tenpin.controller;

import com.example.Tenpin.entity.BoardPost;
import com.example.Tenpin.service.BoardPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/board")
@Tag(name = "Board", description = "자유게시판 API")
public class BoardPostController {

    private final BoardPostService service;

    public BoardPostController(BoardPostService service) {
        this.service = service;
    }

    @Operation(summary = "게시글 작성", description = "새로운 게시글을 작성합니다.")
    @PostMapping("/posts")
    public ResponseEntity<Map<String, String>> createPost(
            @RequestHeader("Authorization") String token,
            @RequestBody BoardPost post) {
        String authorId = extractUserIdFromToken(token); // 토큰에서 사용자 ID 추출
        post.setAuthorId(authorId);
        service.createPost(post);
        return ResponseEntity.ok(Map.of("message", "Post created successfully"));
    }

    @Operation(summary = "게시글 조회", description = "게시글 목록을 조회합니다.")
    @GetMapping("/posts")
    public ResponseEntity<List<BoardPost>> getPosts() {
        List<BoardPost> posts = service.getPosts();
        return ResponseEntity.ok(posts);
    }

    @Operation(summary = "게시글 상세 조회", description = "특정 게시글을 상세 조회합니다.")
    @GetMapping("/posts/{postId}")
    public ResponseEntity<BoardPost> getPostById(@PathVariable Long postId) {
        Optional<BoardPost> post = service.getPostById(postId);
        return post.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "게시글 수정", description = "본인이 작성한 게시글을 수정합니다.")
    @PutMapping("/posts/{postId}")
    public ResponseEntity<Map<String, String>> updatePost(
            @RequestHeader("Authorization") String token,
            @PathVariable Long postId,
            @RequestBody BoardPost updatedPost) {
        String authorId = extractUserIdFromToken(token);
        updatedPost.setPostId(postId);
        updatedPost.setAuthorId(authorId);
        if (service.updatePost(updatedPost)) {
            return ResponseEntity.ok(Map.of("message", "Post updated successfully"));
        } else {
            return ResponseEntity.status(403).body(Map.of("message", "You are not authorized to update this post"));
        }
    }

    @Operation(summary = "게시글 삭제", description = "본인이 작성한 게시글을 삭제합니다.")
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Map<String, String>> deletePost(
            @RequestHeader("Authorization") String token,
            @PathVariable Long postId) {
        String authorId = extractUserIdFromToken(token);
        if (service.deletePost(postId, authorId)) {
            return ResponseEntity.ok(Map.of("message", "Post deleted successfully"));
        } else {
            return ResponseEntity.status(403).body(Map.of("message", "You are not authorized to delete this post"));
        }
    }

    private String extractUserIdFromToken(String token) {
        // JWT 토큰에서 사용자 ID를 추출하는 로직을 구현 (Mock 예시)
        return "mockUserId"; // 실제 구현 시 JWT 파싱 로직으로 대체
    }
}
