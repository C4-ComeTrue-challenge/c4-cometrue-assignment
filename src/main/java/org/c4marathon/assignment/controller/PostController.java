package org.c4marathon.assignment.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.c4marathon.assignment.domain.Member;
import org.c4marathon.assignment.domain.Post;
import org.c4marathon.assignment.domain.request.PostRequest;
import org.c4marathon.assignment.domain.response.PostDetailResponse;
import org.c4marathon.assignment.domain.response.PostResponse;
import org.c4marathon.assignment.service.PostService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;

    @Value("${file.dir}")
    private String fileDir;

    // 게시글 작성 (회원 정보를 세션에서 가져옴)
    @PostMapping("/write")
    public ResponseEntity<?> createPost(@RequestBody PostRequest postRequest,
                                        HttpSession session) {
        postService.createPost(postRequest, session);
        return ResponseEntity.ok("게시글 작성 성공");
    }

    // 게시글 전체 조회
    @GetMapping
    public ResponseEntity<?> getAllPosts(@RequestParam(value = "lastPostId", required = false) Long lastPostId,
                                         @RequestParam(value = "size", defaultValue = "5") int size) {
        List<PostResponse> posts = postService.getAllPosts(lastPostId, size);
        return ResponseEntity.ok(posts);
    }

    // 게시글 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable("id") Post post) {
        return ResponseEntity.ok(new PostDetailResponse(post));
    }

    // 게시글 수정
    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable Long postId, @RequestBody PostRequest postRequest, HttpSession session) {
        postService.updatePost(postId, postRequest, session);
        return ResponseEntity.ok("게시글 수정 성공");
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId, @RequestParam(required = false) String password, HttpSession session) {
        postService.deletePost(postId, session, password);

        return ResponseEntity.ok("게시글 삭제 성공");
    }
}
