package org.c4marathon.assignment.user.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.c4marathon.assignment.AcceptanceTest;
import org.c4marathon.assignment.user.ui.dto.request.SignUpRequest;
import org.hamcrest.core.StringStartsWith;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

class AuthApiTest extends AcceptanceTest {
	@Nested
	@DisplayName("/sign-up")
	class SignUp {
		@Test
		@DisplayName("[201]")
		void shouldReturn201(@Autowired MockMvc mockMvc, @Autowired ObjectMapper objectMapper) throws Exception {
			SignUpRequest request = new SignUpRequest("newEmail", "newPassword", "newName", "customer");
			String expectedMessage = "";

			ResultActions perform = mockMvc.perform(
				post("/sign-up")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)));

			perform.andExpectAll(
				status().isCreated(),
				header().string("Location", new StringStartsWith("/users")),
				content().string(expectedMessage)
			);
		}
	}
}
