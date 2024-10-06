package org.c4marathon.assignment.board.domain.service;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.board.domain.Board;
import org.c4marathon.assignment.board.domain.repository.BoardRepository;
import org.c4marathon.assignment.global.annotation.DomainService;

@DomainService
@RequiredArgsConstructor
public class BoardSaveService {

    private final BoardRepository boardRepository;

    public Board save(Board board) {
        return boardRepository.save(board);

    }
}
