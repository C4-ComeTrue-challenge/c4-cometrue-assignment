package org.c4marathon.assignment.board.service;

import static org.assertj.core.api.Assertions.*;
import static org.c4marathon.assignment.global.dto.DeletionReason.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.c4marathon.assignment.board.domain.Boards;
import org.c4marathon.assignment.board.domain.WriterType;
import org.c4marathon.assignment.board.domain.repository.BoardJpaRepository;
import org.c4marathon.assignment.board.domain.repository.BoardRepository;
import org.c4marathon.assignment.board.dto.BoardCreateRequest;
import org.c4marathon.assignment.board.dto.BoardDeleteRequest;
import org.c4marathon.assignment.board.dto.BoardGetOneResponse;
import org.c4marathon.assignment.board.dto.BoardUpdateRequest;
import org.c4marathon.assignment.img.domain.Img;
import org.c4marathon.assignment.img.domain.repository.ImgJpaRepository;
import org.c4marathon.assignment.img.domain.repository.ImgRepository;
import org.c4marathon.assignment.img.service.S3Service;
import org.c4marathon.assignment.user.domain.Users;
import org.c4marathon.assignment.user.domain.repository.UserJpaRepository;
import org.c4marathon.assignment.user.domain.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class BoardServiceTest {

	@Autowired
	private BoardRepository boardRepository;

	@Autowired
	private BoardJpaRepository boardJpaRepository;

	@Autowired
	private ImgRepository imgRepository;

	@Autowired
	private ImgJpaRepository imgJpaRepository;

	@MockBean
	private S3Service s3Service;

	@Autowired
	@InjectMocks
	private BoardService boardService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserJpaRepository userJpaRepository;

	@MockBean
	private Clock clock;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@AfterEach
	void tearDown() {
		imgJpaRepository.deleteAllInBatch();
		boardJpaRepository.deleteAllInBatch();
		userJpaRepository.deleteAllInBatch();
	}

	private Users createUser() {
		Users user = Users.builder()
			.nickname("Test Writer")
			.email("test@test.com")
			.password("1234")
			.build();
		return userRepository.save(user);
	}

	private void createImg(String imageName) {
		Img img = Img.builder().fileName(imageName).build();
		imgRepository.save(img);
	}

	@DisplayName("회원이 이미지를 포함한 게시글을 성공적으로 생성한다.")
	@Test
	void createBoardAsUserWithImagesSuccess() {
		// Given
		Users user = createUser();
		createImg("image1.jpg");
		when(s3Service.validateUrl("http://validbucket.s3.amazonaws.com/image1.jpg")).thenReturn(true);

		// When
		String contentWithImages = "<p>Test content</p><img src=\"http://validbucket.s3.amazonaws.com/image1.jpg\"/>";
		BoardCreateRequest request = new BoardCreateRequest("Test Title", contentWithImages, null, null);
		Long boardId = boardService.createBoardAsUser(request, user);

		// Then
		Boards savedBoard = boardRepository.getById(boardId);
		List<String> fileName = imgRepository.getFileNamesByBoardId(boardId);
		assertThat(fileName).containsExactlyInAnyOrder("image1.jpg");
		assertThat(savedBoard.getContent()).isEqualTo(contentWithImages);
		assertThat(savedBoard.getWriterName()).isEqualTo("Test Writer");
		assertThat(savedBoard.getWriterType()).isEqualTo(WriterType.USER);
	}

	@DisplayName("비회원이 이미지를 포함한 게시글을 성공적으로 생성한다.")
	@Test
	void createBoardAsGuestWithImagesSuccess() {
		// Given
		String guestWriterName = "Guest Writer";
		String guestPassword = "guest1234";
		createImg("image1.jpg");
		when(s3Service.validateUrl("http://validbucket.s3.amazonaws.com/image1.jpg")).thenReturn(true);

		// When
		String contentWithImages = "<p>Test content</p><img src=\"http://validbucket.s3.amazonaws.com/image1.jpg\"/>";
		BoardCreateRequest request = new BoardCreateRequest("Guest Title", contentWithImages, guestWriterName,
			guestPassword);
		Long boardId = boardService.createBoardAsGuest(request);

		// Then
		Boards savedBoard = boardRepository.getById(boardId);
		List<String> fileName = imgRepository.getFileNamesByBoardId(boardId);
		assertThat(fileName).containsExactlyInAnyOrder("image1.jpg");
		assertThat(savedBoard.getContent()).isEqualTo(contentWithImages);
		assertThat(savedBoard.getWriterName()).isEqualTo(guestWriterName);
		assertThat(savedBoard.getWriterType()).isEqualTo(WriterType.GUEST);
	}

	@DisplayName("회원이 게시글을 성공적으로 수정한다.")
	@Test
	void updateBoardAsUserSuccess() {
		// Given
		Users user = createUser();
		String oldContent = "<p>old content</p><img src=\"http://validbucket.s3.amazonaws.com/image1.jpg\"/>";
		createImg("image1.jpg");
		createImg("image2.jpg");

		when(s3Service.validateUrl("http://validbucket.s3.amazonaws.com/image1.jpg")).thenReturn(true);
		when(s3Service.validateUrl("http://validbucket.s3.amazonaws.com/image2.jpg")).thenReturn(true);
		doNothing().when(s3Service).deleteImages(List.of("image1.jpg"));

		BoardCreateRequest createRequest = new BoardCreateRequest("Old Title", oldContent, null, null);
		Long boardId = boardService.createBoardAsUser(createRequest, user);

		// When
		String updatedContent = "<p>Updated content</p><img src=\"http://validbucket.s3.amazonaws.com/image2.jpg\"/>";
		BoardUpdateRequest updateRequest = new BoardUpdateRequest("Updated Title", updatedContent, null);
		boardService.updateBoardAsUser(boardId, updateRequest, user.getNickname());

		// Then
		verify(s3Service, times(1)).deleteImages(List.of("image1.jpg"));

		Boards updatedBoard = boardRepository.getById(boardId);
		assertThat(updatedBoard.getTitle()).isEqualTo("Updated Title");
		assertThat(updatedBoard.getContent()).isEqualTo(updatedContent);

		List<String> fileNames = imgRepository.getFileNamesByBoardId(boardId);
		assertThat(fileNames).containsExactlyInAnyOrder("image2.jpg");
	}

	@DisplayName("비회원이 게시글을 성공적으로 수정한다.")
	@Test
	void updateBoardAsGuestSuccess() {
		// Given
		String oldContent = "<p>old content</p><img src=\"http://validbucket.s3.amazonaws.com/image1.jpg\"/>";

		createImg("image1.jpg");
		createImg("image2.jpg");

		when(s3Service.validateUrl("http://validbucket.s3.amazonaws.com/image1.jpg")).thenReturn(true);
		when(s3Service.validateUrl("http://validbucket.s3.amazonaws.com/image2.jpg")).thenReturn(true);

		BoardCreateRequest createRequest = new BoardCreateRequest("Old Title", oldContent, "writerName", "password");
		Long boardId = boardService.createBoardAsGuest(createRequest);

		doNothing().when(s3Service).deleteImages(List.of("image1.jpg"));

		// When
		String updatedContent = "<p>Updated content</p><img src=\"http://validbucket.s3.amazonaws.com/image2.jpg\"/>";
		BoardUpdateRequest updateRequest = new BoardUpdateRequest("Updated Title", updatedContent, "password");
		boardService.updateBoardAsGuest(boardId, updateRequest);

		// Then
		verify(s3Service, times(1)).deleteImages(List.of("image1.jpg"));

		Boards updatedBoard = boardRepository.getById(boardId);
		assertThat(updatedBoard.getTitle()).isEqualTo("Updated Title");
		assertThat(updatedBoard.getContent()).isEqualTo(updatedContent);

		List<String> fileNames = imgRepository.getFileNamesByBoardId(boardId);
		assertThat(fileNames).containsExactlyInAnyOrder("image2.jpg");
	}

	@DisplayName("게시글을 ID로 조회하면 성공적으로 게시글 정보를 반환한다.")
	@Test
	void getOneBoardSuccess() {
		// Given
		Boards board = boardRepository.save(
			Boards.builder()
				.content("Test Content")
				.title("Test Title")
				.writerName("Test Writer")
				.writerType(WriterType.USER)
				.build());

		// When
		BoardGetOneResponse response = boardService.getOneBoard(board.getId());

		// Then
		assertThat(response.id()).isEqualTo(board.getId());
		assertThat(response.content()).isEqualTo(board.getContent());
		assertThat(response.title()).isEqualTo(board.getTitle());
		assertThat(response.writerName()).isEqualTo(board.getWriterName());
	}

	@DisplayName("회원이 게시글을 성공적으로 삭제한다.")
	@Test
	void deleteBoardAsUserSuccess() {
		// Given
		Users user = createUser();

		createImg("image1.jpg");

		when(s3Service.validateUrl("http://validbucket.s3.amazonaws.com/image1.jpg")).thenReturn(true);
		doNothing().when(s3Service).deleteImages(List.of("image1.jpg"));

		String contentWithImage = "<p>Test content</p><img src=\"http://validbucket.s3.amazonaws.com/image1.jpg\"/>";
		BoardCreateRequest createRequest = new BoardCreateRequest("Test Title", contentWithImage, null, null);
		Long boardId = boardService.createBoardAsUser(createRequest, user);

		LocalDateTime fixedTime = LocalDateTime.of(2024, 10, 19, 12, 0);
		Instant fixedInstant = fixedTime.toInstant(ZoneOffset.UTC);
		when(clock.instant()).thenReturn(fixedInstant);
		when(clock.getZone()).thenReturn(ZoneOffset.UTC);

		// When
		boardService.deleteBoardAsUser(boardId, user.getNickname());

		// Then
		verify(s3Service, times(1)).deleteImages(List.of("image1.jpg"));
		List<String> deletedImages = imgRepository.getFileNamesByBoardId(boardId);
		assertThat(deletedImages).isEmpty();

		Boards deletedBoard = boardRepository.getById(boardId);
		assertTrue(deletedBoard.isDeleted());
		assertEquals(DELETED_BY_MEMBER.getMessage(), deletedBoard.getDeletionReason());
		assertEquals(fixedTime, deletedBoard.getDeletedDate());
	}

	@DisplayName("비회원이 게시글을 성공적으로 삭제한다.")
	@Test
	void deleteBoardAsGuestSuccess() {
		// Given
		createImg("image1.jpg");

		when(s3Service.validateUrl("http://validbucket.s3.amazonaws.com/image1.jpg")).thenReturn(true);
		doNothing().when(s3Service).deleteImages(List.of("image1.jpg"));

		String contentWithImage = "<p>Test content</p><img src=\"http://validbucket.s3.amazonaws.com/image1.jpg\"/>";
		BoardCreateRequest createRequest = new BoardCreateRequest("Test Title", contentWithImage, "writerName",
			"password");
		Long boardId = boardService.createBoardAsGuest(createRequest);

		LocalDateTime fixedTime = LocalDateTime.of(2024, 10, 19, 12, 0);
		Instant fixedInstant = fixedTime.toInstant(ZoneOffset.UTC);
		when(clock.instant()).thenReturn(fixedInstant);
		when(clock.getZone()).thenReturn(ZoneOffset.UTC);

		// When
		BoardDeleteRequest deleteRequest = new BoardDeleteRequest("password");
		boardService.deleteBoardAsGuest(boardId, deleteRequest);

		// Then
		verify(s3Service, times(1)).deleteImages(List.of("image1.jpg"));
		List<String> deletedImages = imgRepository.getFileNamesByBoardId(boardId);
		assertThat(deletedImages).isEmpty();

		Boards deletedBoard = boardRepository.getById(boardId);
		assertTrue(deletedBoard.isDeleted());
		assertEquals(DELETED_BY_MEMBER.getMessage(), deletedBoard.getDeletionReason());
		assertEquals(fixedTime, deletedBoard.getDeletedDate());
	}

}
