package org.c4marathon.assignment.board.domain.repository;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.board.domain.Board;
import org.c4marathon.assignment.board.exception.NotFoundBoardException;
import org.c4marathon.assignment.board.presentation.dto.BoardResponse;
import org.c4marathon.assignment.board.service.dto.BoardCreateServiceRequest;
import org.c4marathon.assignment.board.service.dto.BoardUpdateServiceRequest;
import org.c4marathon.assignment.global.exception.ErrorCode;
import org.c4marathon.assignment.user.exception.DuplicateNameException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardRepository {
    private final BoardJpaRepository boardJpaRepository;

    public BoardResponse createBoard(BoardCreateServiceRequest request) {

        if (validateNameDuplicate(request.name())) {
            throw new DuplicateNameException(ErrorCode.DUPLICATE_NAME);
        }

        Board board = Board.of(request.name());
        boardJpaRepository.save(board);

        return new BoardResponse(board.getId(), board.getName());
    }

    @Transactional(readOnly = true)
    public List<BoardResponse> getAllBoard() {

        List<Board> boards = boardJpaRepository.findAll();

        return boards.stream()
                .map(board -> new BoardResponse(board.getId(), board.getName()))
                .toList();
    }

    public BoardResponse updateBoardName(BoardUpdateServiceRequest request) {

        if (validateNameDuplicate(request.name())) {
            throw new DuplicateNameException(ErrorCode.DUPLICATE_NAME);
        }

        Board board = boardJpaRepository.findById(request.boardId())
                .orElseThrow(() -> new NotFoundBoardException(ErrorCode.NOT_FOUND_BOARD));

        board.changeBoardName(request.name());

        return new BoardResponse(board.getId(), board.getName());
    }

    /*public void deleteBoard(BoardDeleteServiceRequest request) {

        Board board = boardRepository.findById(request.boardId())
                .orElseThrow(() -> new NotFoundBoardException(ErrorCode.NOT_FOUND_BOARD));

        boardRepository.deleteById(board.getId());

    }*/

    private boolean validateNameDuplicate(String name) {
        return boardJpaRepository.existsByName(name);
    }
}
