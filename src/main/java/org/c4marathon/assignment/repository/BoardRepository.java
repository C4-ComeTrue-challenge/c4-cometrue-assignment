package org.c4marathon.assignment.repository;

import org.c4marathon.assignment.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
