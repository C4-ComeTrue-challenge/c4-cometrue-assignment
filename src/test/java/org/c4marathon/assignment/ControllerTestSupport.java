package org.c4marathon.assignment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.c4marathon.assignment.board.presentation.BoardController;
import org.c4marathon.assignment.board.service.BoardService;
import org.c4marathon.assignment.user.presentation.UserController;
import org.c4marathon.assignment.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {
        BoardController.class,
        UserController.class
})
public class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected BoardService boardService;

    @MockBean
    protected UserService userService;
}
