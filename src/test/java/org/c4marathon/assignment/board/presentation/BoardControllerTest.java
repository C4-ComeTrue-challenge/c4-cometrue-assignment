package org.c4marathon.assignment.board.presentation;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

import org.c4marathon.assignment.board.dto.BoardCreateRequest;
import org.c4marathon.assignment.board.dto.BoardDeleteRequest;
import org.c4marathon.assignment.board.dto.BoardGetAllResponse;
import org.c4marathon.assignment.board.dto.BoardGetOneResponse;
import org.c4marathon.assignment.board.dto.BoardUpdateRequest;
import org.c4marathon.assignment.board.dto.PageInfo;
import org.c4marathon.assignment.board.service.BoardService;
import org.c4marathon.assignment.config.CommonControllerTest;
import org.c4marathon.assignment.global.config.SessionConfig;
import org.c4marathon.assignment.user.domain.Users;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(controllers = BoardController.class)
class BoardControllerTest extends CommonControllerTest {

	@MockBean
	private BoardService boardService;

	@MockBean
	private SessionConfig sessionConfig;

	@Test
	@DisplayName("POST /api/board 요청 시 회원이 게시글을 성공적으로 작성하면 201 CREATED 상태를 반환한다.")
	void createBoardAsUserSuccess() throws Exception {
		// Given
		BoardCreateRequest createRequest = new BoardCreateRequest("Test Title", "Test Content", null, null);
		Users mockUser = Users.builder().email("test@example.com").nickname("testUser").build();

		Mockito.when(sessionConfig.getSessionUser(any())).thenReturn(mockUser);

		// When & Then
		mockMvc.perform(post("/api/board")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createRequest)))
			.andExpect(status().isCreated());

		Mockito.verify(boardService, Mockito.times(1)).createBoardAsUser(any(BoardCreateRequest.class), eq(mockUser));
	}

	@Test
	@DisplayName("POST /api/board 요청 시 비회원이 게시글을 작성할 때 필요한 정보가 없으면 400 Bad Request 상태를 반환한다.")
	void createBoardAsGuestFailMissingInfo() throws Exception {
		// Given
		BoardCreateRequest createRequest = new BoardCreateRequest("Test Title", "Test Content", null, null);

		// When & Then
		mockMvc.perform(post("/api/board")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createRequest)))
			.andExpect(status().isBadRequest());

		Mockito.verify(boardService, Mockito.times(0)).createBoardAsGuest(any(BoardCreateRequest.class));
	}

	@Test
	@DisplayName("GET /api/board/all 요청 시 성공적으로 게시글 목록을 조회하면 200 OK 상태를 반환한다.")
	void getAllBoardsSuccess() throws Exception {
		// Given
		List<BoardGetAllResponse> boardResponses = List.of(
			new BoardGetAllResponse(1L, "Title 1", "Content 1", "Writer 1", LocalDateTime.now(), LocalDateTime.now()),
			new BoardGetAllResponse(2L, "Title 2", "Content 2", "Writer 2", LocalDateTime.now(), LocalDateTime.now())
		);
		PageInfo<BoardGetAllResponse> mockPageInfo = new PageInfo<>("nextPageToken", boardResponses, true);
		
		Mockito.when(boardService.getAllBoards(anyString(), anyInt())).thenReturn(mockPageInfo);

		// When & Then: GET 요청을 보내고 응답을 검증합니다.
		mockMvc.perform(get("/api/board/all")
				.param("pageToken", "nextPageToken")
				.param("count", "10"))
			.andExpect(jsonPath("$.data.size()").value(2))  // 게시글 목록이 2개인지 확인
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.hasNext").value(true))     // 다음 페이지가 있는지 확인
			.andExpect(jsonPath("$.pageToken").value("nextPageToken"));  // 페이지 토큰이 반환되는지 확인

		Mockito.verify(boardService, Mockito.times(1)).getAllBoards(anyString(), anyInt());
	}

	@Test
	@DisplayName("GET /api/board/{id} 요청 시 게시글을 성공적으로 조회하면 200 OK 상태를 반환한다.")
	void getOneBoardSuccess() throws Exception {
		// Given
		BoardGetOneResponse response = new BoardGetOneResponse(1L, "Test Content", "Test Title", "Test Writer", null,
			null);

		Mockito.when(boardService.getOneBoard(anyLong())).thenReturn(response);

		// When & Then
		mockMvc.perform(get("/api/board/{id}", 1L))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("Test Title"))
			.andExpect(jsonPath("$.content").value("Test Content"));

		Mockito.verify(boardService, Mockito.times(1)).getOneBoard(anyLong());
	}

	@Test
	@DisplayName("PUT /api/board/{id} 요청 시 회원이 게시글을 성공적으로 수정하면 201 CREATED 상태를 반환한다.")
	void updateBoardAsUserSuccess() throws Exception {
		// Given
		BoardUpdateRequest updateRequest = new BoardUpdateRequest("Updated Title", "Updated Content", null);
		Users mockUser = Users.builder().email("test@example.com").nickname("testUser").build();

		Mockito.when(sessionConfig.getSessionUser(any())).thenReturn(mockUser);

		// When & Then
		mockMvc.perform(put("/api/board/{id}", 1L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateRequest)))
			.andExpect(status().isCreated());

		Mockito.verify(boardService, Mockito.times(1))
			.updateBoardAsUser(anyLong(), any(BoardUpdateRequest.class), eq(mockUser.getNickname()));
	}

	@Test
	@DisplayName("PUT /api/board/{id} 요청 시 비회원이 게시글을 수정할 때 비밀번호가 없으면 400 Bad Request 상태를 반환한다.")
	void updateBoardAsGuestFailMissingPassword() throws Exception {
		// Given
		BoardUpdateRequest updateRequest = new BoardUpdateRequest("Updated Title", "Updated Content", null);

		// When & Then
		mockMvc.perform(put("/api/board/{id}", 1L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateRequest)))
			.andExpect(status().isBadRequest());

		Mockito.verify(boardService, Mockito.times(0)).updateBoardAsGuest(anyLong(), any(BoardUpdateRequest.class));
	}

	@Test
	@DisplayName("DELETE /api/board/{id} 요청 시 회원이 게시글을 성공적으로 삭제하면 201 CREATED 상태를 반환한다.")
	void deleteBoardAsUserSuccess() throws Exception {
		// Given
		Users mockUser = Users.builder().email("test@example.com").nickname("testUser").build();
		BoardDeleteRequest deleteRequest = new BoardDeleteRequest(null);

		Mockito.when(sessionConfig.getSessionUser(any())).thenReturn(mockUser);

		// When & Then
		mockMvc.perform(delete("/api/board/{id}", 1L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(deleteRequest)))
			.andExpect(status().isCreated());

		Mockito.verify(boardService, Mockito.times(1)).deleteBoardAsUser(anyLong(), eq(mockUser.getNickname()));
	}

	@Test
	@DisplayName("DELETE /api/board/{id} 요청 시 비회원이 게시글을 삭제할 때 비밀번호가 없으면 400 Bad Request 상태를 반환한다.")
	void deleteBoardAsGuestFailMissingPassword() throws Exception {
		// Given
		BoardDeleteRequest deleteRequest = new BoardDeleteRequest(null);

		// When & Then
		mockMvc.perform(delete("/api/board/{id}", 1L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(deleteRequest)))
			.andExpect(status().isBadRequest());

		Mockito.verify(boardService, Mockito.times(0)).deleteBoardAsGuest(anyLong(), any(BoardDeleteRequest.class));
	}
}
