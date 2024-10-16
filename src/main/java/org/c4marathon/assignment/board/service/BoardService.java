package org.c4marathon.assignment.board.service;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.board.domain.Board;
import org.c4marathon.assignment.board.domain.repository.BoardRepository;
import org.c4marathon.assignment.board.exception.NotFoundBoardException;
import org.c4marathon.assignment.board.presentation.dto.BoardResponse;
import org.c4marathon.assignment.board.service.dto.BoardCreateServiceRequest;
import org.c4marathon.assignment.board.service.dto.BoardUpdateServiceRequest;
import org.c4marathon.assignment.user.exception.DuplicateNameException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardResponse createBoard(BoardCreateServiceRequest request) {

        if (validateNameDuplicate(request.name())) {
            throw new DuplicateNameException();
        }

        Board board = Board.create(request.name());
        boardRepository.save(board);

        return new BoardResponse(board.getId(), board.getName());
    }

    private boolean validateNameDuplicate(String name) {
        return boardRepository.existsByName(name);
    }

    @Transactional(readOnly = true)
    public List<BoardResponse> getAllBoard() {

        List<Board> boards = boardRepository.findAll();

        return boards.stream()
                .map(board -> new BoardResponse(board.getId(), board.getName()))
                .toList();
    }

    public BoardResponse updateBoardName(BoardUpdateServiceRequest request) {

        if (validateNameDuplicate(request.name())) {
            throw new DuplicateNameException();
        }

        Board board = boardRepository.findById(request.boardId())
                .orElseThrow(NotFoundBoardException::new);

        board.changeBoardName(request.name());

        return new BoardResponse(board.getId(), board.getName());
    }

    /*public void deleteBoard(BoardDeleteServiceRequest request) {

        Board board = boardRepository.findById(request.boardId())
                .orElseThrow(() -> new NotFoundBoardException(ErrorCode.NOT_FOUND_BOARD));

        boardRepository.deleteById(board.getId());

    }*/
}