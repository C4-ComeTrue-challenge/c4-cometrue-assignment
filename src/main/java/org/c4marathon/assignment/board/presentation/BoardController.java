package org.c4marathon.assignment.board.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.board.presentation.dto.BoardCreateRequest;
import org.c4marathon.assignment.board.presentation.dto.BoardResponse;
import org.c4marathon.assignment.board.presentation.dto.BoardUpdateRequest;
import org.c4marathon.assignment.board.service.BoardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class BoardController {
    private final BoardService boardService;

    @PostMapping("/v1/board")
    public ResponseEntity<BoardResponse> createBoard(@Valid @RequestBody BoardCreateRequest request) {

        BoardResponse board = boardService.createBoard(request.toServiceDto());

        return ResponseEntity.status(HttpStatus.CREATED).body(board);
    }

    @GetMapping("/v1/board")
    public ResponseEntity<List<BoardResponse>> getBoards() {
        List<BoardResponse> boards = boardService.getAllBoard();

        return ResponseEntity.ok(boards);
    }

    @PutMapping("/v1/board")
    public ResponseEntity<BoardResponse> updateBoard(@Valid @RequestBody BoardUpdateRequest request) {
        BoardResponse board = boardService.updateBoardName(request.toServiceDto());

        return ResponseEntity.ok(board);
    }
}
