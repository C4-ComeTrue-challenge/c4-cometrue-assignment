package org.c4marathon.assignment.board.service;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.board.domain.Board;
import org.c4marathon.assignment.board.domain.service.BoardGetService;
import org.c4marathon.assignment.board.domain.service.BoardSaveService;
import org.c4marathon.assignment.board.dto.BoardCreateRequest;
import org.c4marathon.assignment.board.dto.BoardGetAllResponse;
import org.c4marathon.assignment.board.dto.BoardGetOneResponse;
import org.c4marathon.assignment.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardSaveService boardSaveService;
    private final BoardGetService boardGetService;
    private final BCryptPasswordEncoder encoder;

    @Transactional
    public void createBoardAsUser(BoardCreateRequest request, User user) {
        Board board = toBoard(request, user);
        boardSaveService.save(board);
    }

    @Transactional
    public void createBoardAsGuest(BoardCreateRequest request) {
        Board board = toBoard(request);
        boardSaveService.save(board);
    }

    @Transactional(readOnly = true)
    public Page<BoardGetAllResponse> getAllBoards(Pageable pageable) {
        return boardGetService.getAll(pageable);
    }

    @Transactional(readOnly = true)
    public BoardGetOneResponse getOneBoard(Long id) {
        Board board = boardGetService.getById(id);
        return toDto(board);
    }

    private Board toBoard(BoardCreateRequest request, User user) {
        return new Board(
                request.title(),
                request.content(),
                user
        );
    }

    private Board toBoard(BoardCreateRequest request) {
        return new Board(
                request.title(),
                request.content(),
                request.writerName(),
                encoder.encode(request.password())
        );
    }

    private BoardGetOneResponse toDto(Board board) {
        return new BoardGetOneResponse(
                board.getId(),
                board.getContent(),
                board.getTitle(),
                board.getWriterName(),
                board.getCreatedDate(),
                board.getLastModifiedDate()
        );
    }

}
