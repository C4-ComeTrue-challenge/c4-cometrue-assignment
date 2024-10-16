package org.c4marathon.assignment.board.domain.repository;

import static org.assertj.core.api.Assertions.*;

import org.c4marathon.assignment.board.domain.Board;
import org.c4marathon.assignment.board.dto.BoardGetAllResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class BoardJpaRepositoryTest {

	@Autowired
	private BoardJpaRepository boardJpaRepository;

	@AfterEach
	void tearDown() {
		boardJpaRepository.deleteAllInBatch();
	}

	@DisplayName("게시글 페이징 조회가 성공적으로 동작한다.")
	@Test
	void findAllWithPagingSuccess() {
		// Given: 5개의 게시글을 저장
		for (int i = 1; i <= 5; i++) {
			Board board = Board.builder()
				.title("Title " + i)
				.content("Content " + i)
				.writerName("Writer " + i)
				.build();
			boardJpaRepository.save(board);
		}

		Pageable pageable = PageRequest.of(0, 3);  // 1페이지에 3개씩 가져오는 페이지 요청

		// When
		Page<BoardGetAllResponse> page = boardJpaRepository.findAllWithPaging(pageable);

		// Then
		assertThat(page).isNotNull();
		assertThat(page.getTotalElements()).isEqualTo(5);
		assertThat(page.getTotalPages()).isEqualTo(2);
		assertThat(page.getNumberOfElements()).isEqualTo(3);

		assertThat(page.getContent().get(0).writerName()).isEqualTo("Writer 5");
		assertThat(page.getContent().get(1).writerName()).isEqualTo("Writer 4");
		assertThat(page.getContent().get(2).writerName()).isEqualTo("Writer 3");
	}
}
