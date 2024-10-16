package org.c4marathon.assignment.board.domain.service;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.c4marathon.assignment.board.domain.Board;
import org.c4marathon.assignment.board.domain.repository.BoardJpaRepository;
import org.c4marathon.assignment.board.domain.repository.BoardRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BoardSaveServiceTest {

	@Autowired
	private BoardRepository boardRepository;

	@Autowired
	private BoardJpaRepository boardJpaRepository;

	@AfterEach
	void tearDown() {
		boardJpaRepository.deleteAllInBatch();
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
		Board savedBoard = boardRepository.save(board);

		// Then
		assertThat(savedBoard).isNotNull();
		assertThat(savedBoard.getId()).isNotNull();
		assertThat(savedBoard.getTitle()).isEqualTo("Test Title");
		assertThat(savedBoard.getContent()).isEqualTo("Test Content");
		assertThat(savedBoard.getWriterName()).isEqualTo("Test Writer");

		// 데이터베이스에 저장된 게시글 확인
		Optional<Board> foundBoard = boardJpaRepository.findById(savedBoard.getId());
		assertThat(foundBoard).isPresent();
		assertThat(foundBoard.get().getTitle()).isEqualTo("Test Title");
		assertThat(foundBoard.get().getContent()).isEqualTo("Test Content");
	}
}
