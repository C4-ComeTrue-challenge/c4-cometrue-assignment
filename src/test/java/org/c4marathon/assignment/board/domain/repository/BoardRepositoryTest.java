package org.c4marathon.assignment.board.domain.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.c4marathon.assignment.board.domain.Boards;
import org.c4marathon.assignment.board.domain.WriterType;
import org.c4marathon.assignment.board.dto.BoardGetAllResponse;
import org.c4marathon.assignment.board.dto.PageInfo;
import org.c4marathon.assignment.board.exception.NotFoundBoardException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BoardRepositoryTest {

	@Autowired
	private BoardJpaRepository boardJpaRepository;

	@Autowired
	private BoardRepository boardRepository;

	@BeforeEach
	void setUp() {
		List<Boards> boards = new ArrayList<>();
		for (int i = 1; i <= 20; i++) {
			Boards board = Boards.builder()
				.title("Test Title " + i)
				.content("Test Content " + i)
				.writerName("Test Writer " + i)
				.writerType(WriterType.USER)
				.build();
			boards.add(board);
		}
		boardJpaRepository.saveAllAndFlush(boards);
	}

	@AfterEach
	void tearDown() {
		boardJpaRepository.deleteAllInBatch();
	}

	@DisplayName("페이지 토큰 없이 게시글을 성공적으로 조회한다.")
	@Test
	void findBoardsWithoutPageTokenSuccess() {
		// Given
		int size = 10; // 한 페이지에 10개의 게시글을 가져옴

		// When
		PageInfo<BoardGetAllResponse> pageInfo = boardRepository.findBoardsWithoutPageToken(size);

		// Then
		assertThat(pageInfo.data()).hasSize(size);  // 요청한 크기만큼 데이터가 조회되는지 확인
		assertThat(pageInfo.hasNext()).isTrue();  // 20개 중 10개만 조회했으므로 다음 페이지가 있어야 함
		assertThat(pageInfo.pageToken()).isNotNull();  // 페이지 토큰이 생성되어야 함
	}

	@DisplayName("게시글 조회 시 페이지 토큰이 없는 경우 첫 페이지를 성공적으로 가져온다.")
	@Test
	void findBoardsWithEmptyPageTokenReturnsFirstPage() {
		// Given
		int size = 10;

		// When
		PageInfo<BoardGetAllResponse> pageInfo = boardRepository.findBoardsWithoutPageToken(size);

		// Then
		assertThat(pageInfo.data()).hasSize(size);
		assertThat(pageInfo.hasNext()).isTrue();  // 아직 다음 페이지가 있어야 함
		assertThat(pageInfo.pageToken()).isNotNull();
	}

	@DisplayName("페이지 토큰을 사용하여 다음 페이지의 게시글을 성공적으로 조회한다.")
	@Test
	void findBoardsWithPageTokenSuccess() {
		// Given
		int size = 10;

		// 첫 페이지 조회
		PageInfo<BoardGetAllResponse> firstPage = boardRepository.findBoardsWithoutPageToken(size);
		String firstPageToken = firstPage.pageToken();

		// When
		PageInfo<BoardGetAllResponse> secondPage = boardRepository.findBoardsWithPageToken(firstPageToken, size);

		// Then
		assertThat(secondPage.data()).hasSizeLessThanOrEqualTo(size);  // 두 번째 페이지에도 size 이하의 게시글이 조회되는지 확인
		if (secondPage.hasNext()) {
			assertThat(secondPage.pageToken()).isNotNull();  // 다음 페이지가 있으면 페이지 토큰이 있어야 함
		} else {
			assertThat(secondPage.pageToken()).isNull();  // 마지막 페이지이므로 페이지 토큰은 null이어야 함
		}
	}

	@DisplayName("게시글을 ID로 성공적으로 조회한다.")
	@Test
	void getByIdSuccess() {
		// Given
		Boards board = Boards.builder()
			.title("Test Title")
			.content("Test Content")
			.writerName("Test Writer")
			.writerType(WriterType.USER)
			.build();
		Boards savedBoard = boardRepository.save(board);  // 게시글 저장

		// When
		Boards foundBoard = boardRepository.getById(savedBoard.getId());

		// Then
		assertThat(foundBoard).isNotNull();
		assertThat(foundBoard.getTitle()).isEqualTo("Test Title");
		assertThat(foundBoard.getContent()).isEqualTo("Test Content");
		assertThat(foundBoard.getWriterName()).isEqualTo("Test Writer");
	}

	@DisplayName("존재하지 않는 게시글 ID로 조회하면 NotFoundBoardException을 던진다.")
	@Test
	void getByIdNotFound() {
		// Given
		Long nonExistentId = 999L;

		// When & Then
		assertThrows(NotFoundBoardException.class, () -> {
			boardRepository.getById(nonExistentId);
		});
	}

	@DisplayName("게시글을 성공적으로 저장한다.")
	@Test
	void saveBoardSuccess() {
		// Given
		Boards board = Boards.builder()
			.title("New Title")
			.content("New Content")
			.writerName("New Writer")
			.writerType(WriterType.USER)
			.build();

		// When
		Boards savedBoard = boardRepository.save(board);

		// Then
		assertThat(savedBoard.getId()).isNotNull();
		assertThat(savedBoard.getTitle()).isEqualTo("New Title");
		assertThat(savedBoard.getContent()).isEqualTo("New Content");
		assertThat(savedBoard.getWriterName()).isEqualTo("New Writer");
	}

}

