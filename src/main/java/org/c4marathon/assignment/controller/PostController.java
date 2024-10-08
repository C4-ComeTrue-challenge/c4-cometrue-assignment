package org.c4marathon.assignment.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.domain.Member;
import org.c4marathon.assignment.domain.Post;
import org.c4marathon.assignment.domain.request.PostRequest;
import org.c4marathon.assignment.domain.response.PostDetailResponse;
import org.c4marathon.assignment.domain.response.PostResponse;
import org.c4marathon.assignment.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    // 게시글 작성 (회원 정보를 세션에서 가져옴)
    @PostMapping("/write")
    public ResponseEntity<?> createPost(@RequestBody PostRequest postRequest, HttpSession session) {
        return Optional.ofNullable((Member) session.getAttribute("member"))
                .map(member -> {
                    postService.createPost(postRequest, member);
                    return ResponseEntity.ok("게시글 작성 성공");
                })
                .orElseGet(() -> ResponseEntity.status(401).body("로그인이 필요합니다."));
    }

    // 게시글 전체 조회
    @GetMapping
    public ResponseEntity<?> getAllPosts() {
        List<PostResponse> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    // 게시글 단일 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable("id") Post post) {
        return ResponseEntity.ok(new PostDetailResponse(post));
    }
}
