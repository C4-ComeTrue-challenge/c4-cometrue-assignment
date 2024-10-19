package org.c4marathon.assignment.seller.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.c4marathon.assignment.AcceptanceTest;
import org.c4marathon.assignment.seller.ui.dto.request.SignUpRequest;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

class SellerApiTest extends AcceptanceTest {
	@Nested
	@DisplayName("POST /sellers")
	class SignUp {
		@Test
		@DisplayName("[201]")
		void shouldReturn201(@Autowired MockMvc mockMvc, @Autowired ObjectMapper objectMapper) throws Exception {
			SignUpRequest request = new SignUpRequest("newEmail", "newPassword", "newName", "000-0000-000");
			String expectedMessage = "";

			ResultActions perform = mockMvc.perform(
				post("/sellers")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)));

			perform.andExpectAll(
				status().isCreated(),
				header().string("Location", matcherForEndWithUuid()),
				content().string(expectedMessage)
			);
		}

		private Matcher<String> matcherForEndWithUuid() {
			return Matchers.matchesRegex(
				"^/sellers/([0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12})$");
		}
	}
}
