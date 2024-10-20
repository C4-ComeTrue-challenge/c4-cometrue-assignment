package org.c4marathon.assignment.board.service;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.board.domain.repository.BoardRepository;
import org.c4marathon.assignment.board.presentation.dto.BoardResponse;
import org.c4marathon.assignment.board.service.dto.BoardCreateServiceRequest;
import org.c4marathon.assignment.board.service.dto.BoardUpdateServiceRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardResponse createBoard(BoardCreateServiceRequest request) {
        return boardRepository.createBoard(request);
    }

    @Transactional(readOnly = true)
    public List<BoardResponse> getAllBoard() {
        return boardRepository.getAllBoard();
    }

    public BoardResponse updateBoardName(BoardUpdateServiceRequest request) {
        return boardRepository.updateBoardName(request);
    }

    /*public void deleteBoard(BoardDeleteServiceRequest request) {

        Board board = boardRepository.findById(request.boardId())
                .orElseThrow(() -> new NotFoundBoardException(ErrorCode.NOT_FOUND_BOARD));

        boardRepository.deleteById(board.getId());

    }*/

}
