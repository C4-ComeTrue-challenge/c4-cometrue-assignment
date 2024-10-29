package org.c4marathon.assignment.img.domain.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

	@DisplayName("이미지를 성공적으로 저장한다.")
	@Test
	void saveImageSuccess() {
		// Given
		String fileName = "newImage.jpg";
		Img img = Img.builder().fileName(fileName).build();

		// When
		imgRepository.save(img);

		// Then
		Img foundImg = imgRepository.getById(img.getId());
		assertThat(foundImg.getFileName()).isEqualTo(fileName);
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
		String[] fileName = new String[3];
		for (int i = 0; i < 3; i++) {
			fileName[i] = "bulkDeleteImage" + (i + 1) + ".jpg";
		}
		List<Img> imgs = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			imgs.add(imgRepository.save(Img.builder().fileName(fileName[i]).build()));
		}
		// When: 파일 이름 리스트를 사용해 다수의 이미지를 삭제
		imgRepository.deleteByFileNames(List.of(fileName));

		// Then: 삭제된 파일들이 더 이상 존재하지 않는지 확인
		for (int i = 0; i < 3; i++) {
			Optional<Img> notDeletedImg = imgJpaRepository.findNotDeletedById(imgs.get(i).getId());
			assertThat(notDeletedImg).isEmpty();
		}
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
