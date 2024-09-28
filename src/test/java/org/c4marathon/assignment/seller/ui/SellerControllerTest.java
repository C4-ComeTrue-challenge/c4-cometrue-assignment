package org.c4marathon.assignment.seller.ui;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import org.c4marathon.assignment.seller.application.SignUpSellerService;
import org.c4marathon.assignment.seller.ui.dto.request.SignUpRequest;
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

@WebMvcTest(controllers = SellerController.class)
class SellerControllerTest {
	@MockBean
	private SignUpSellerService signUpSellerService;

	@Nested
	@DisplayName("/sellers")
	class SignUp {
		@Test
		@DisplayName("[201]")
		void shouldReturn201(@Autowired MockMvc mockMvc, @Autowired ObjectMapper objectMapper) throws Exception {
			UUID nextId = UUID.randomUUID();

			SignUpRequest request = new SignUpRequest("newEmail", "newPassword", "newName", "000-0000-000");
			String expectedMessage = "";

			when(signUpSellerService.register(
				eq(new SignUpSellerService.Command(request.email(), request.password(), request.name(),
					request.licenseNumber()))))
				.thenReturn(nextId);

			ResultActions perform = mockMvc.perform(
				post("/sellers")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)));

			perform.andExpectAll(
				status().isCreated(),
				header().string("Location", "/sellers/" + nextId),
				content().string(expectedMessage)
			);
			verify(signUpSellerService).register(
				eq(new SignUpSellerService.Command(request.email(), request.password(), request.name(),
					request.licenseNumber())));
		}
	}
}
