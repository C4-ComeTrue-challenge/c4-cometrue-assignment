package org.c4marathon.assignment.board.domain.service;

import org.c4marathon.assignment.board.domain.Board;
import org.c4marathon.assignment.board.domain.repository.BoardRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BoardSaveServiceTest {

    @Autowired
    private BoardSaveService boardSaveService;

    @Autowired
    private BoardRepository boardRepository;

    @AfterEach
    void tearDown() {
        boardRepository.deleteAllInBatch();
    }

    @DisplayName("게시글을 성공적으로 저장한다.")
    @Test
    void saveBoardSuccess() {
        // Given
        Board board = Board.builder()
                .title("Test Title")
                .content("Test Content")
                .writerName("Test Writer")
                .build();

        // When
        Board savedBoard = boardSaveService.save(board);

        // Then
        assertThat(savedBoard).isNotNull();
        assertThat(savedBoard.getId()).isNotNull();
        assertThat(savedBoard.getTitle()).isEqualTo("Test Title");
        assertThat(savedBoard.getContent()).isEqualTo("Test Content");
        assertThat(savedBoard.getWriterName()).isEqualTo("Test Writer");

        // 데이터베이스에 저장된 게시글 확인
        Optional<Board> foundBoard = boardRepository.findById(savedBoard.getId());
        assertThat(foundBoard).isPresent();
        assertThat(foundBoard.get().getTitle()).isEqualTo("Test Title");
        assertThat(foundBoard.get().getContent()).isEqualTo("Test Content");
    }
}
