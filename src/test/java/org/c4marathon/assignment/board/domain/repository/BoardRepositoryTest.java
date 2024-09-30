package org.c4marathon.assignment.board.domain.repository;

import org.c4marathon.assignment.board.domain.Board;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @AfterEach
    void tearDown() {
        boardRepository.deleteAllInBatch();
    }

    @DisplayName("종복된 게시판 이름을 조회하면 true을 반환한다.")
    @Test
    void existsBoardName() throws Exception {
        // given
        Board board = Board.create("test");
        boardRepository.save(board);

        // when
        boolean result = boardRepository.existsByName(board.getName());

        // then
        assertThat(result).isTrue();
    }

}