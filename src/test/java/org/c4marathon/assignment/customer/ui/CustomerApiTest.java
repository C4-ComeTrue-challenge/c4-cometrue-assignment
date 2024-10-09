package org.c4marathon.assignment.customer.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.c4marathon.assignment.AcceptanceTest;
import org.c4marathon.assignment.customer.ui.dto.request.SignUpRequest;
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

class CustomerApiTest extends AcceptanceTest {
	@Nested
	@DisplayName("POST /customers")
	class SignUp {
		@Test
		@DisplayName("[201]")
		void shouldReturn201(@Autowired MockMvc mockMvc, @Autowired ObjectMapper objectMapper) throws Exception {
			SignUpRequest request = new SignUpRequest("newEmail", "newPassword", "newName");
			String expectedMessage = "";

			ResultActions perform = mockMvc.perform(
				post("/customers")
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
				"^/customers/([0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12})$");
		}
	}
}
