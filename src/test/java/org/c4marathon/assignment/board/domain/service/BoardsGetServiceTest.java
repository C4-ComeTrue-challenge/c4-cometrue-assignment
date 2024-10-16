package org.c4marathon.assignment.board.domain.service;

import static org.assertj.core.api.Assertions.*;

import org.c4marathon.assignment.board.domain.Boards;
import org.c4marathon.assignment.board.domain.repository.BoardJpaRepository;
import org.c4marathon.assignment.board.domain.repository.BoardRepository;
import org.c4marathon.assignment.board.dto.BoardGetAllResponse;
import org.c4marathon.assignment.board.exception.NotFoundBoardException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class BoardsGetServiceTest {

	@Autowired
	private BoardRepository boardRepository;

	@Autowired
	private BoardJpaRepository boardJpaRepository;

	@AfterEach
	void tearDown() {
		boardJpaRepository.deleteAllInBatch();
	}

	@DisplayName("게시글 페이징 조회가 성공적으로 동작한다.")
	@Test
	void getAllBoardsSuccess() {
		// Given
		for (int i = 1; i <= 5; i++) {
			Boards boards = Boards.builder()
				.title("Title " + i)
				.content("Content " + i)
				.writerName("Writer " + i)
				.build();
			boardJpaRepository.save(boards);
		}

		Pageable pageable = PageRequest.of(0, 3);

		// When
		Page<BoardGetAllResponse> page = boardRepository.getAll(pageable);

		// Then
		assertThat(page).isNotNull();
		assertThat(page.getTotalElements()).isEqualTo(5);
		assertThat(page.getTotalPages()).isEqualTo(2);
		assertThat(page.getNumberOfElements()).isEqualTo(3);

		assertThat(page.getContent().get(0).writerName()).isEqualTo("Writer 5");
		assertThat(page.getContent().get(1).writerName()).isEqualTo("Writer 4");
		assertThat(page.getContent().get(2).writerName()).isEqualTo("Writer 3");
	}

	@DisplayName("ID로 게시글을 성공적으로 조회한다.")
	@Test
	void getByIdSuccess() {
		// Given
		Boards boards = Boards.builder()
			.title("Test Title")
			.content("Test Content")
			.writerName("Test Writer")
			.build();
		boards = boardJpaRepository.save(boards);

		// When
		Boards foundBoards = boardRepository.getById(boards.getId());

		// Then
		assertThat(foundBoards).isNotNull();
		assertThat(foundBoards.getTitle()).isEqualTo("Test Title");
		assertThat(foundBoards.getContent()).isEqualTo("Test Content");
		assertThat(foundBoards.getWriterName()).isEqualTo("Test Writer");
	}

	@DisplayName("존재하지 않는 ID로 조회 시 예외가 발생한다.")
	@Test
	void getByIdNotFound() {
		// When & Then
		assertThatThrownBy(() -> boardRepository.getById(999L))
			.isInstanceOf(NotFoundBoardException.class);
	}
}
