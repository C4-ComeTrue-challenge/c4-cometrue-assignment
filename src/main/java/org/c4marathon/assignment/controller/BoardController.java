package org.c4marathon.assignment.controller;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.domain.Board;
import org.c4marathon.assignment.domain.Member;
import org.c4marathon.assignment.domain.Notice;
import org.c4marathon.assignment.domain.request.BoardRequest;
import org.c4marathon.assignment.domain.request.NoticeRequest;
import org.c4marathon.assignment.domain.response.BoardResponse;
import org.c4marathon.assignment.domain.response.NoticeResponse;
import org.c4marathon.assignment.service.BoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<?> createBoard(@RequestBody BoardRequest request, @SessionAttribute("member") Member member) {
        Board board = boardService.createBoard(request.getName(), request.getDescription(), member);
        return ResponseEntity.ok(new BoardResponse(board));
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<?> updateBoardMetadata(
            @PathVariable Long boardId,
            @RequestBody BoardRequest request,
            @SessionAttribute("member") Member member) {
        boardService.updateBoardMetadata(boardId, request.getName(), request.getDescription(), member);
        return ResponseEntity.ok("게시판 정보가 업데이트되었습니다.");
    }

    @DeleteMapping("/{boardId}/posts/{postId}")
    public ResponseEntity<?> deletePost(
            @PathVariable Long boardId,
            @PathVariable Long postId,
            @SessionAttribute("member") Member member) {
        boardService.deletePost(boardId, postId, member);
        return ResponseEntity.ok("게시글이 삭제되었습니다.");
    }

    @PostMapping("/{boardId}/notices")
    public ResponseEntity<?> addNotice(
            @PathVariable Long boardId,
            @RequestBody NoticeRequest request,
            @SessionAttribute("member") Member member) {
        Notice notice = boardService.addNotice(boardId, request.getTitle(), request.getContent(), member);
        return ResponseEntity.ok(new NoticeResponse(notice));
    }
}
