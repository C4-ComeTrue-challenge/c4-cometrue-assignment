package org.c4marathon.assignment.board.domain.repository;

import org.c4marathon.assignment.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardJpaRepository extends JpaRepository<Board, Long> {

    boolean existsByName(String name);
}
