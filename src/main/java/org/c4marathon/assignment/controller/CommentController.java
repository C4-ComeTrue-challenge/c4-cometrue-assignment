package org.c4marathon.assignment.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.domain.request.CommentRequest;
import org.c4marathon.assignment.domain.response.CommentResponse;
import org.c4marathon.assignment.service.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts/{postId}/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@PathVariable Long postId, @RequestBody CommentRequest commentRequest,
            HttpSession session) {
        CommentResponse commentResponse = commentService.createComment(postId, commentRequest, session);
        return ResponseEntity.ok(commentResponse);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Long commentId, @RequestBody CommentRequest commentRequest,
            HttpSession session) {
        CommentResponse commentResponse = commentService.updateComment(commentId, commentRequest, session);
        return ResponseEntity.ok(commentResponse);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable Long commentId, @RequestBody CommentRequest commentRequest, HttpSession session) {
        commentService.deleteComment(commentId,commentRequest ,session);
        return ResponseEntity.ok("댓글 삭제 완료");
    }

    @GetMapping
    public ResponseEntity<?> getComments(
            @PathVariable Long postId,
            @PageableDefault(size = 100) Pageable pageable) {
        Page<CommentResponse> comments = commentService.getComments(postId, pageable);
        return ResponseEntity.ok(comments);
    }
}
