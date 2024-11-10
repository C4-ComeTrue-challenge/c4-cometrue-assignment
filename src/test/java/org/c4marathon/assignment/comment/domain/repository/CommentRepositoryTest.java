package org.c4marathon.assignment.comment.domain.repository;

import static org.assertj.core.api.Assertions.*;
import static org.c4marathon.assignment.board.domain.WriterType.*;

import org.c4marathon.assignment.board.domain.Boards;
import org.c4marathon.assignment.board.domain.repository.BoardJpaRepository;
import org.c4marathon.assignment.comment.domain.Comment;
import org.c4marathon.assignment.comment.dto.CommentGetAllResponse;
import org.c4marathon.assignment.comment.exception.NotFoundCommentException;
import org.c4marathon.assignment.global.dto.PageInfo;
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
class CommentRepositoryTest {

	@Autowired
	private CommentJpaRepository commentJpaRepository;

	@Autowired
	private BoardJpaRepository boardJpaRepository;

	private CommentRepository commentRepository;

	@BeforeEach
	void setUp() {
		commentRepository = new CommentRepository(commentJpaRepository);
	}

	@AfterEach
	void tearDown() {
		commentJpaRepository.deleteAllInBatch();
		boardJpaRepository.deleteAllInBatch();
	}

	@DisplayName("페이지 토큰 없이 댓글 목록을 조회할 수 있다.")
	@Test
	void findCommentsWithoutPageTokenSucess() {
		Boards testBoard = boardJpaRepository.save(Boards.builder()
			.title("Test Board Title")
			.content("Test Board Content")
			.writerName("Test Writer")
			.password("1234")
			.writerType(GUEST)
			.build());

		// Given
		for (int i = 0; i < 5; i++) {
			Comment comment = Comment.builder()
				.content("Comment " + i)
				.writerName("Writer " + i)
				.board(testBoard)
				.password("1234")
				.writerType(GUEST)
				.build();
			commentRepository.save(comment);
		}

		// When
		PageInfo<CommentGetAllResponse> pageInfo = commentRepository.findCommentsWithoutPageToken(testBoard.getId(), 3);

		// Then
		assertThat(pageInfo.data()).hasSize(3);
		assertThat(pageInfo.hasNext()).isTrue();  // 아직 다음 페이지가 있어야 함
		assertThat(pageInfo.pageToken()).isNotNull();
		assertThat(pageInfo.data().get(0).content()).isEqualTo("Comment 4");
	}

	@DisplayName("페이지 토큰을 사용하여 댓글 목록을 조회할 수 있다.")
	@Test
	void findCommentsWithPageTokeSucess() {
		// Given
		Boards testBoard = boardJpaRepository.save(Boards.builder()
			.title("Test Board Title")
			.content("Test Board Content")
			.writerName("Test Writer")
			.password("1234")
			.writerType(GUEST)
			.build());

		// Given
		for (int i = 0; i < 20; i++) {
			Comment comment = Comment.builder()
				.content("Comment " + i)
				.writerName("Writer " + i)
				.board(testBoard)
				.password("1234")
				.writerType(GUEST)
				.path(null)
				.build();
			commentRepository.save(comment);
		}

		PageInfo<CommentGetAllResponse> firstPage = commentRepository.findCommentsWithoutPageToken(testBoard.getId(),
			10);
		String firstPageToken = firstPage.pageToken();
		//When
		PageInfo<CommentGetAllResponse> secondPage = commentRepository.findCommentsWithPageToken(testBoard.getId(),
			firstPageToken,
			10);

		// Then
		assertThat(secondPage.data()).hasSizeLessThanOrEqualTo(10);  // 두 번째 페이지에도 size 이하의 게시글이 조회되는지 확인
		if (secondPage.hasNext()) {
			assertThat(secondPage.pageToken()).isNotNull();  // 다음 페이지가 있으면 페이지 토큰이 있어야 함
		} else {
			assertThat(secondPage.pageToken()).isNull();  // 마지막 페이지이므로 페이지 토큰은 null이어야 함
		}

	}

	@DisplayName("댓글을 저장할 수 있다.")
	@Test
	void saveCommentSuccess() {

		// Given
		Boards testBoard = boardJpaRepository.save(Boards.builder()
			.title("Test Board Title")
			.content("Test Board Content")
			.writerName("Test Writer")
			.password("1234")
			.writerType(GUEST)
			.build());

		// When
		Comment comment = commentRepository.save(Comment.builder()
			.content("Test Comment")
			.writerName("Test Writer")
			.board(testBoard)
			.password("1234")
			.writerType(GUEST)
			.build());

		// Then
		assertThat(commentRepository.getById(comment.getId()).getContent()).isEqualTo("Test Comment");
	}

	@DisplayName("ID로 삭제되지 않은 댓글을 조회할 수 있다.")
	@Test
	void getByIdNotDeletedSucess() {
		// Given

		Boards testBoard = boardJpaRepository.save(Boards.builder()
			.title("Test Board Title")
			.content("Test Board Content")
			.writerName("Test Writer")
			.password("1234")
			.writerType(GUEST)
			.build());

		Comment comment = Comment.builder()
			.content("Test Comment")
			.writerName("Test Writer")
			.board(testBoard)
			.password("1234")
			.writerType(GUEST)
			.build();
		commentRepository.save(comment);

		// When
		Comment retrievedComment = commentRepository.getById(comment.getId());

		// Then
		assertThat(retrievedComment).isNotNull();
		assertThat(retrievedComment.getContent()).isEqualTo("Test Comment");
	}

	@DisplayName("존재하지 않는 댓글을 조회하면 예외가 발생한다.")
	@Test
	void getByIdNotFoundFail() {
		// When & Then
		assertThatThrownBy(() -> commentRepository.getById(999L))
			.isInstanceOf(NotFoundCommentException.class);
	}

}
