package org.c4marathon.assignment.board.service;

import org.c4marathon.assignment.board.domain.Board;
import org.c4marathon.assignment.board.domain.repository.BoardRepository;
import org.c4marathon.assignment.board.domain.service.BoardGetService;
import org.c4marathon.assignment.board.dto.BoardCreateRequest;
import org.c4marathon.assignment.board.dto.BoardGetAllResponse;
import org.c4marathon.assignment.board.dto.BoardGetOneResponse;
import org.c4marathon.assignment.board.exception.NotFoundBoardException;
import org.c4marathon.assignment.user.domain.User;
import org.c4marathon.assignment.user.domain.repository.UserRepository;
import org.c4marathon.assignment.user.domain.service.UserSaveService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.c4marathon.assignment.global.exception.ErrorCode.NOT_FOUND_BOARD_ERROR;

@SpringBootTest
class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardGetService boardGetService;

    @Autowired
    private UserSaveService userSaveService;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @AfterEach
    void tearDown() {
        boardRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("회원이 게시글을 성공적으로 생성한다.")
    @Test
    void createBoardAsUserSuccess() {
        // Given
        User user = User.builder()
                .email("test@test.com")
                .password(encoder.encode("password"))
                .nickname("testNickname")
                .build();

        user = userSaveService.save(user);

        BoardCreateRequest request = new BoardCreateRequest(
                "Test Title",
                "Test Content",
                null,
                null);

        // When
        long id = boardService.createBoardAsUser(request, user);

        // Then
        Board board = boardGetService.getById(id);  // 첫 번째로 생성된 게시글 조회
        assertThat(board).isNotNull();
        assertThat(board.getTitle()).isEqualTo("Test Title");
        assertThat(board.getContent()).isEqualTo("Test Content");
        assertThat(board.getWriterName()).isEqualTo("testNickname");
    }

    @DisplayName("비회원이 게시글을 성공적으로 생성한다.")
    @Test
    void createBoardAsGuestSuccess() {
        // Given
        BoardCreateRequest request = new BoardCreateRequest(
                "Test Title",
                "Test Content",
                "GuestUser",
                "password");

        // When
        long id = boardService.createBoardAsGuest(request);

        // Then
        Board board = boardGetService.getById(id);  // 첫 번째로 생성된 게시글 조회
        assertThat(board).isNotNull();
        assertThat(board.getTitle()).isEqualTo("Test Title");
        assertThat(board.getContent()).isEqualTo("Test Content");
        assertThat(board.getWriterName()).isEqualTo("GuestUser");
    }

    @DisplayName("게시글을 ID로 성공적으로 조회한다.")
    @Test
    void getOneBoardSuccess() {
        // Given
        BoardCreateRequest request = new BoardCreateRequest(
                "Test Title",
                "Test Content",
                "GuestUser",
                "password");
        long id = boardService.createBoardAsGuest(request);

        // When
        BoardGetOneResponse response = boardService.getOneBoard(id);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.title()).isEqualTo("Test Title");
        assertThat(response.content()).isEqualTo("Test Content");
        assertThat(response.writerName()).isEqualTo("GuestUser");
    }

    @DisplayName("게시글이 존재하지 않을 경우 예외가 발생한다.")
    @Test
    void getOneBoardNotFound() {
        // When & Then
        assertThatThrownBy(() -> boardService.getOneBoard(999L))
                .isInstanceOf(NotFoundBoardException.class)
                .hasMessageContaining(NOT_FOUND_BOARD_ERROR.getMessage());
    }

    @DisplayName("게시글 목록을 성공적으로 조회한다.")
    @Test
    void getAllBoardsSuccess() {
        // Given
        boardService.createBoardAsGuest(new BoardCreateRequest(
                "Title1",
                "Content1",
                "GuestUser1",
                "password1"));

        boardService.createBoardAsGuest(new BoardCreateRequest(
                "Title2",
                "Content2",
                "GuestUser2",
                "password2"));

        // When
        Page<BoardGetAllResponse> responsePage = boardService.getAllBoards(
                PageRequest.of(0, 10));

        // Then
        assertThat(responsePage.getContent().size()).isEqualTo(2);
        assertThat(responsePage.getContent().get(0).title()).isEqualTo("Content2");
        assertThat(responsePage.getContent().get(1).title()).isEqualTo("Content1");
    }

}
