package org.c4marathon.assignment.img.domain.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.c4marathon.assignment.board.domain.Boards;
import org.c4marathon.assignment.board.domain.WriterType;
import org.c4marathon.assignment.board.domain.repository.BoardJpaRepository;
import org.c4marathon.assignment.img.domain.Img;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ImgRepositoryTest {

	@Autowired
	private ImgJpaRepository imgJpaRepository;

	@Autowired
	private ImgRepository imgRepository;

	@Autowired
	private BoardJpaRepository boardRepository;

	@AfterEach
	void tearDown() {
		imgJpaRepository.deleteAllInBatch();
	}

	@DisplayName("파일명이 존재하는지 성공적으로 확인한다.")
	@Test
	void existsByFileNameSuccess() {
		// Given
		String fileName = "testImage.jpg";
		Img img = Img.builder().fileName(fileName).build();
		imgRepository.save(img);

		// When
		boolean exists = imgRepository.existsByFileName(fileName);

		// Then
		assertThat(exists).isTrue();
	}

	@DisplayName("존재하지 않는 파일명으로 존재 여부를 확인하면 false를 반환한다.")
	@Test
	void existsByFileNameNotFound() {
		// When
		boolean exists = imgRepository.existsByFileName("nonexistentImage.jpg");

		// Then
		assertThat(exists).isFalse();
	}

	@DisplayName("이미지를 성공적으로 저장한다.")
	@Test
	void saveImageSuccess() {
		// Given
		String fileName = "newImage.jpg";
		Img img = Img.builder().fileName(fileName).build();

		// When
		imgRepository.save(img);

		// Then
		boolean exists = imgRepository.existsByFileName(fileName);
		assertThat(exists).isTrue();
	}

	@DisplayName("게시판 ID로 파일명을 성공적으로 조회한다.")
	@Test
	void findFileNamesByBoardIdSuccess() {
		// Given
		Boards board = boardRepository.save(Boards.builder()
			.title("Test Title 2")
			.content("Test Content 2")
			.writerName("Test Writer")
			.writerType(WriterType.USER)
			.build());

		Img img1 = Img.builder().fileName("boardImage1.jpg").board(board).build();
		Img img2 = Img.builder().fileName("boardImage2.jpg").board(board).build();

		imgRepository.save(img1);
		imgRepository.save(img2);

		// When
		List<String> fileNames = imgRepository.getFileNamesByBoardId(board.getId());

		// Then
		assertThat(fileNames).containsExactlyInAnyOrder("boardImage1.jpg", "boardImage2.jpg");
	}

	@DisplayName("파일명 리스트로 여러 개의 이미지를 성공적으로 삭제한다.")
	@Test
	void bulkDeleteByFileNamesSuccess() {
		// Given
		String fileName1 = "bulkDeleteImage1.jpg";
		String fileName2 = "bulkDeleteImage2.jpg";
		String fileName3 = "bulkDeleteImage3.jpg";
		Img img1 = Img.builder().fileName(fileName1).build();
		Img img2 = Img.builder().fileName(fileName2).build();
		Img img3 = Img.builder().fileName(fileName3).build();
		imgJpaRepository.saveAll(List.of(img1, img2, img3));

		// When: 파일 이름 리스트를 사용해 다수의 이미지를 삭제
		imgRepository.deleteByFileNames(List.of(fileName1, fileName2));

		// Then: 삭제된 파일들이 더 이상 존재하지 않는지 확인
		assertThat(imgJpaRepository.existsByFileName(fileName1)).isFalse();
		assertThat(imgJpaRepository.existsByFileName(fileName2)).isFalse();
		assertThat(imgJpaRepository.existsByFileName(fileName3)).isTrue();  // 삭제하지 않은 파일은 여전히 존재
	}

	@DisplayName("파일명 리스트로 여러 개의 이미지의 게시판 정보를 일괄 업데이트한다.")
	@Test
	void bulkUpdateBoardByFileNamesSuccess() {
		// Given
		Boards board1 = boardRepository.save(Boards.builder()
			.title("Test Title 1")
			.content("Test Content 1")
			.writerName("Test Writer")
			.writerType(WriterType.USER)
			.build());

		Boards board2 = boardRepository.save(Boards.builder()
			.title("Test Title 2")
			.content("Test Content 2")
			.writerName("Test Writer")
			.writerType(WriterType.USER)
			.build());

		String fileName1 = "bulkUpdateBoardImage1.jpg";
		String fileName2 = "bulkUpdateBoardImage2.jpg";
		String fileName3 = "bulkUpdateBoardImage3.jpg";
		Img img1 = Img.builder().fileName(fileName1).board(board1).build();
		Img img2 = Img.builder().fileName(fileName2).board(board1).build();
		Img img3 = Img.builder().fileName(fileName3).board(board1).build();
		imgJpaRepository.saveAll(List.of(img1, img2, img3));

		imgRepository.setBoardByFileName(List.of(fileName1, fileName2), board2);

		List<String> updatedFileNames = imgRepository.getFileNamesByBoardId(board2.getId());
		assertThat(updatedFileNames).containsExactlyInAnyOrder(fileName1, fileName2);

		List<String> remainingFileNames = imgRepository.getFileNamesByBoardId(board1.getId());
		assertThat(remainingFileNames).containsExactly(fileName3);
	}
}
