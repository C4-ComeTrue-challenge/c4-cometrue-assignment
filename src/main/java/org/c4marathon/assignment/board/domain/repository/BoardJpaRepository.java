package org.c4marathon.assignment.board.domain.repository;

import org.c4marathon.assignment.board.domain.Boards;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardJpaRepository extends JpaRepository<Boards, Long>, BoardCustomRepository {

}
