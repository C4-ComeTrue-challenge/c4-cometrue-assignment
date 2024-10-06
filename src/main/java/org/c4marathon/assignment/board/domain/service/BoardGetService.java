package org.c4marathon.assignment.board.domain.service;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.board.domain.Board;
import org.c4marathon.assignment.board.domain.repository.BoardRepository;
import org.c4marathon.assignment.board.dto.BoardGetAllResponse;
import org.c4marathon.assignment.board.exception.NotFoundBoardException;
import org.c4marathon.assignment.global.annotation.DomainService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@DomainService
@RequiredArgsConstructor
public class BoardGetService {

    private final BoardRepository boardRepository;

    public Page<BoardGetAllResponse> getAll(Pageable pageable) {
        return boardRepository.findAllWithPaging(pageable);
    }

    public Board getById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new NotFoundBoardException());
    }
}
