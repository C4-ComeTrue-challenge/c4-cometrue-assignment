package org.c4marathon.assignment.board.service;

import org.c4marathon.assignment.IntegrationTestSupport;
import org.c4marathon.assignment.board.domain.Board;
import org.c4marathon.assignment.board.domain.repository.BoardRepository;
import org.c4marathon.assignment.board.presentation.dto.BoardResponse;
import org.c4marathon.assignment.board.service.dto.BoardCreateServiceRequest;
import org.c4marathon.assignment.board.service.dto.BoardUpdateServiceRequest;
import org.c4marathon.assignment.user.exception.DuplicateNameException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class BoardServiceTest extends IntegrationTestSupport {

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardRepository boardRepository;

    @AfterEach
    void tearDown() {
        boardRepository.deleteAllInBatch();
    }

    @DisplayName("게시판의 이름을 설정해 게시판을 만들 수 있다.")
    @Test
    void createBoard() throws Exception {
        // given
        BoardCreateServiceRequest request = new BoardCreateServiceRequest("test");

        // when
        Long boardId = boardService.createBoard(request);

        // then
        assertThat(boardId).isNotNull();
    }

    @DisplayName("이미 있는 게시판의 이름으로 생성하면 예외가 발생한다.")
    @Test
    void createBoardByDuplicateName() throws Exception {
        // given
        Board board = Board.create("test");
        boardRepository.save(board);

        BoardCreateServiceRequest request = new BoardCreateServiceRequest("test");

        // when // then
        assertThatThrownBy(() -> boardService.createBoard(request))
                .isInstanceOf(DuplicateNameException.class)
                .hasMessage("중복된 게시판 이름 입니다.");
    }

    @DisplayName("게시판을 모두 조회한다.")
    @Test
    void getAllBoard() throws Exception {
        // given
        Board board = Board.create("test");
        Board board1 = Board.create("test1");
        Board board2 = Board.create("test2");
        Board board3 = Board.create("test3");

        boardRepository.saveAll(List.of(board, board1, board2, board3));

        // when
        List<BoardResponse> allBoard = boardService.getAllBoard();

        // then
        assertThat(allBoard).hasSize(4)
                .extracting("name")
                .containsExactlyInAnyOrder(
                        "test", "test1", "test2", "test3"
                );
    }

    @DisplayName("게시판의 이름을 수정할 수 있다.")
    @Test
    void changeName() throws Exception {
        // given
        Board board = Board.create("test");
        boardRepository.save(board);

        BoardUpdateServiceRequest request = new BoardUpdateServiceRequest(board.getId(), "changeName");

        // when
        BoardResponse updateBoard = boardService.updateBoardName(request);

        // then
        assertThat(updateBoard.name()).isEqualTo("changeName");
    }
    @DisplayName("이미 있는 이름으로 게시판을 수정할 경우 예외가 발생한다.")
    @Test
    void changeNameByDuplicateName() throws Exception {
        // given
        Board board = Board.create("test");
        Board board1 = Board.create("test1");
        boardRepository.saveAll(List.of(board, board1));

        BoardUpdateServiceRequest request = new BoardUpdateServiceRequest(board.getId(), "test1");

        // when // then
        assertThatThrownBy(() -> boardService.updateBoardName(request))
                .isInstanceOf(DuplicateNameException.class)
                .hasMessage("중복된 게시판 이름 입니다.");
    }

}