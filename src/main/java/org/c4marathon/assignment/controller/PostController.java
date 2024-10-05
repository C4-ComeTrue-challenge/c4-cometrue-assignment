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

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    // 게시글 작성 (회원 정보를 세션에서 가져옴)
    @PostMapping("/write")
    public ResponseEntity<?> createPost(@RequestBody PostRequest postRequest, HttpSession session) {
        // 세션에서 로그인된 사용자 정보 확인
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            return ResponseEntity.status(401).body(null); // 로그인된 사용자가 없을 경우 401 Unauthorized 반환
        }

        // 게시글 작성
        Post post = postService.createPost(postRequest, member);

        return ResponseEntity.ok(post);
    }

    // 게시글 전체 조회
    @GetMapping
    public ResponseEntity<?> getAllPosts() {
        List<PostResponse> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    // 게시글 단일 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        PostDetailResponse post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }
}
