package org.c4marathon.assignment.board.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.c4marathon.assignment.ControllerTestSupport;
import org.c4marathon.assignment.board.presentation.dto.BoardCreateRequest;
import org.c4marathon.assignment.board.presentation.dto.BoardResponse;
import org.c4marathon.assignment.board.presentation.dto.BoardUpdateRequest;
import org.c4marathon.assignment.board.service.BoardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BoardControllerTest extends ControllerTestSupport {

    @DisplayName("게시판 생성한다.")
    @Test
    void createBoard() throws Exception {
        // given
        BoardCreateRequest request = new BoardCreateRequest("test");

        // when // then
        mockMvc.perform(
                post("/api/v1/board")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated());

    }

    @DisplayName("게시판을 조회한다.")
    @Test
    void getBoards() throws Exception {
        // given
        List<BoardResponse> result = List.of(
                new BoardResponse(1L, "test1"),
                new BoardResponse(2L, "test2")
        );

        when(boardService.getAllBoard()).thenReturn(result);

        // when // then
        mockMvc.perform(
                        get("/api/v1/board")

                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].boardId").value(1L))
                .andExpect(jsonPath("$[0].name").value("test1"))
                .andExpect(jsonPath("$[1].boardId").value(2L))
                .andExpect(jsonPath("$[1].name").value("test2"));
    }

    @DisplayName("게시판 이름을 수정한다.")
    @Test
    void updateBoardName() throws Exception {
        // given
        BoardUpdateRequest request = new BoardUpdateRequest(1L, "update name");
        BoardResponse response = new BoardResponse(1L, "update name");

        when(boardService.updateBoardName(request.toServiceDto())).thenReturn(response);
        // when // then

        mockMvc.perform(
                        put("/api/v1/board")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.boardId").value(1L))
                .andExpect(jsonPath("$.name").value("update name"));
    }
}