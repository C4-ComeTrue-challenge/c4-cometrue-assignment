package org.c4marathon.assignment.board.presentatition;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.board.dto.BoardCreateRequest;
import org.c4marathon.assignment.board.dto.BoardGetAllResponse;
import org.c4marathon.assignment.board.dto.BoardGetOneResponse;
import org.c4marathon.assignment.board.service.BoardService;
import org.c4marathon.assignment.global.config.SessionConfig;
import org.c4marathon.assignment.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/board")
@RequiredArgsConstructor
@RestController
public class BoardController {

    private final BoardService boardService;
    private final SessionConfig sessionConfig;

    @PostMapping
    public ResponseEntity<Void> createBoard(
            @Valid @RequestBody BoardCreateRequest request,
            HttpServletRequest httpServletRequest
    ) {
        User loginUser = sessionConfig.getSessionUser(httpServletRequest);

        if (loginUser == null) {
            if (request.writerName() == null || request.password() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            boardService.createBoardAsGuest(request);
        } else {
            boardService.createBoardAsUser(request, loginUser);
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/all")
    public ResponseEntity<Page<BoardGetAllResponse>> getAllBoards(
            Pageable pageable
    ) {
        Page<BoardGetAllResponse> response = boardService.getAllBoards(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardGetOneResponse> getOneBoard(
            @PathVariable Long id
    ) {
        BoardGetOneResponse response = boardService.getOneBoard(id);
        return ResponseEntity.ok(response);
    }

}
