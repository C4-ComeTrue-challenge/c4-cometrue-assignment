package org.c4marathon.assignment.board.domain.repository;

import org.c4marathon.assignment.board.dto.BoardGetAllResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardCustomRepository {

    Page<BoardGetAllResponse> findAllWithPaging(Pageable pageable);

}
