package org.c4marathon.assignment.board.presentation;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;

import org.c4marathon.assignment.board.dto.BoardCreateRequest;
import org.c4marathon.assignment.board.dto.BoardGetAllResponse;
import org.c4marathon.assignment.board.dto.BoardGetOneResponse;
import org.c4marathon.assignment.board.service.BoardService;
import org.c4marathon.assignment.config.CommonControllerTest;
import org.c4marathon.assignment.global.config.SessionConfig;
import org.c4marathon.assignment.user.domain.Users;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;

import jakarta.servlet.http.HttpServletRequest;

@WebMvcTest(controllers = BoardController.class)
public class BoardControllerTest extends CommonControllerTest {

	@MockBean
	private BoardService boardService;

	@MockBean
	private SessionConfig sessionConfig;

	@Nested
	@DisplayName("POST /api/board")
	class CreateBoardTest {

		@Test
		@DisplayName("게스트 사용자가 게시물을 작성할 수 있다.")
		void guestCanCreateBoard() throws Exception {
			BoardCreateRequest request = new BoardCreateRequest("제목", "내용", "guest", "password");

			mockMvc.perform(post("/api/board")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isCreated());

			verify(boardService, times(1)).createBoardAsGuest(any(BoardCreateRequest.class));
		}

		@Test
		@DisplayName("로그인 사용자가 게시물을 작성할 수 있다.")
		void loggedInUserCanCreateBoard() throws Exception {
			Users mockUser = Users.builder()
				.email("test@example.com")
				.password("password123")
				.nickname("testUser")
				.build();

			BoardCreateRequest request = new BoardCreateRequest("제목", "내용", null, null);

			Mockito.when(sessionConfig.getSessionUser(any(HttpServletRequest.class))).thenReturn(mockUser);

			mockMvc.perform(post("/api/board")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isCreated());

			verify(boardService, times(1)).createBoardAsUser(any(BoardCreateRequest.class), any(Users.class));
		}

		@Test
		@DisplayName("게스트 사용자가 이름과 비밀번호를 제공하지 않으면 400 에러를 발생시킨다.")
		void guestWithoutNameAndPasswordShouldFail() throws Exception {
			BoardCreateRequest request = new BoardCreateRequest("제목", "내용", null, null);

			mockMvc.perform(post("/api/board")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest());

			verify(boardService, times(0)).createBoardAsGuest(any(BoardCreateRequest.class));
		}
	}

	@Nested
	@DisplayName("GET /api/board/all")
	class GetAllBoardsTest {

		@Test
		@DisplayName("모든 게시물을 페이지로 가져온다.")
		void getAllBoards() throws Exception {
			Page<BoardGetAllResponse> page = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0);

			Mockito.when(boardService.getAllBoards(any(PageRequest.class))).thenReturn(page);

			mockMvc.perform(get("/api/board/all")
					.param("page", "0")
					.param("size", "10"))
				.andExpect(status().isOk());

			verify(boardService, times(1)).getAllBoards(any(PageRequest.class));
		}
	}

	@Nested
	@DisplayName("GET /api/board/{id}")
	class GetOneBoardTest {

		@Test
		@DisplayName("특정 ID의 게시물을 가져온다.")
		void getOneBoard() throws Exception {
			BoardGetOneResponse response = new BoardGetOneResponse(1L, "내용", "제목", "guest", null, null);

			Mockito.when(boardService.getOneBoard(1L)).thenReturn(response);

			mockMvc.perform(get("/api/board/1"))
				.andExpect(status().isOk());

			verify(boardService, times(1)).getOneBoard(1L);
		}
	}
}
