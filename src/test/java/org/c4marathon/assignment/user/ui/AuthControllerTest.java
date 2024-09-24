package org.c4marathon.assignment.user.ui;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import org.c4marathon.assignment.user.application.SignUpService;
import org.c4marathon.assignment.user.ui.dto.request.SignUpRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = AuthController.class)
class AuthControllerTest {
	@MockBean
	SignUpService signUpService;

	@Nested
	@DisplayName("/sign-up")
	class SignUp {
		@Test
		@DisplayName("[201]")
		void shouldReturn201(@Autowired MockMvc mockMvc, @Autowired ObjectMapper objectMapper) throws Exception {
			UUID nextId = UUID.randomUUID();

			SignUpRequest request = new SignUpRequest("newEmail", "newPassword", "newName", "customer");
			String expectedMessage = "";

			when(signUpService.registerUser(
				eq(new SignUpService.Command(request.email(), request.password(), request.name(), request.type()))))
				.thenReturn(nextId);

			ResultActions perform = mockMvc.perform(
				post("/sign-up")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)));

			perform.andExpectAll(
				status().isCreated(),
				header().string("Location", "/users/" + nextId),
				content().string(expectedMessage)
			);
			verify(signUpService).registerUser(
				eq(new SignUpService.Command(request.email(), request.password(), request.name(), request.type())));
		}
	}
}
