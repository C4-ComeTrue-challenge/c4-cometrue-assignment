package org.c4marathon.assignment.customer.ui;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import org.c4marathon.assignment.customer.application.SignUpService;
import org.c4marathon.assignment.customer.ui.dto.request.SignUpRequest;
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

@WebMvcTest(controllers = CustomerController.class)
class CustomerControllerTest {
	@MockBean
	SignUpService signUpService;

	@Nested
	@DisplayName("POST /customers")
	class SignUp {
		@Test
		@DisplayName("[201]")
		void shouldReturn201(@Autowired MockMvc mockMvc, @Autowired ObjectMapper objectMapper) throws Exception {
			UUID nextId = UUID.randomUUID();

			SignUpRequest request = new SignUpRequest("newEmail", "newPassword", "newName");
			String expectedMessage = "";

			when(signUpService.register(
				eq(new SignUpService.Command(request.email(), request.password(), request.name()))))
				.thenReturn(nextId);

			ResultActions perform = mockMvc.perform(
				post("/customers")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)));

			perform.andExpectAll(
				status().isCreated(),
				header().string("Location", "/customers/" + nextId),
				content().string(expectedMessage)
			);
			verify(signUpService).register(
				eq(new SignUpService.Command(request.email(), request.password(), request.name())));
		}
	}
}
