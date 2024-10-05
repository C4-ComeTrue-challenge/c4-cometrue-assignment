package org.c4marathon.assignment.product.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import org.c4marathon.assignment.AcceptanceTest;
import org.c4marathon.assignment.common.authentication.model.principal.LoginSeller;
import org.c4marathon.assignment.global.session.SessionConst;
import org.c4marathon.assignment.product.ui.dto.request.RegisterProductRequest;
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

class ProductApiTest extends AcceptanceTest {

	@Nested
	@DisplayName("POST /products")
	class SignUp {
		@Test
		@DisplayName("[201]")
		void shouldReturn201(@Autowired MockMvc mockMvc, @Autowired ObjectMapper objectMapper) throws Exception {
			RegisterProductRequest request = new RegisterProductRequest("newProduct", "this is new product", 10000L,
				20L);
			String expectedMessage = "";

			ResultActions perform = mockMvc.perform(
				post("/products")
					.contentType(MediaType.APPLICATION_JSON)
					.sessionAttr(SessionConst.SESSION_AUTHENTICATION_PRINCIPAL_KEY, new LoginSeller(UUID.randomUUID()))
					.content(objectMapper.writeValueAsString(request)));

			perform.andExpectAll(
				status().isCreated(),
				header().string("Location", matcherForEndWithUuid()),
				content().string(expectedMessage)
			);
		}

		@Test
		@DisplayName("[401]")
		void shouldReturn401(@Autowired MockMvc mockMvc, @Autowired ObjectMapper objectMapper) throws Exception {
			RegisterProductRequest request = new RegisterProductRequest("newProduct", "this is new product", 10000L,
				20L);
			String expectedMessage = "";

			ResultActions perform = mockMvc.perform(
				post("/products")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)));

			perform.andExpectAll(
				status().isUnauthorized(),
				content().string(expectedMessage)
			);
		}

		private Matcher<String> matcherForEndWithUuid() {
			return Matchers.matchesRegex(
				"^/products/\\d+$");
		}
	}
}
