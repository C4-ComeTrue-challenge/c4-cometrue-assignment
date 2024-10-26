package org.c4marathon.assignment.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.domain.request.CommentRequest;
import org.c4marathon.assignment.domain.response.CommentResponse;
import org.c4marathon.assignment.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts/{postId}/comments")
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

}
