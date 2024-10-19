package org.c4marathon.assignment.order.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import org.c4marathon.assignment.AcceptanceTest;
import org.c4marathon.assignment.common.authentication.model.principal.LoginCustomer;
import org.c4marathon.assignment.global.session.SessionConst;
import org.c4marathon.assignment.order.ui.dto.request.AddToCartRequest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

class OrderApiTest extends AcceptanceTest {

	@Nested
	@DisplayName("POST /carts/my/addToCart")
	class AddToCart {
		@Test
		@DisplayName("[200]")
		void shouldReturn200(@Autowired MockMvc mockMvc, @Autowired ObjectMapper objectMapper) throws Exception {
			Long productId = 10L;
			int quantity = 2;
			Integer productVersion = 3;
			AddToCartRequest request = new AddToCartRequest(productId, quantity, productVersion);
			String expectedMessage = "";

			ResultActions perform = mockMvc.perform(
				post("/carts/my/addToCart")
					.contentType(MediaType.APPLICATION_JSON)
					.sessionAttr(SessionConst.SESSION_AUTHENTICATION_PRINCIPAL_KEY,
						new LoginCustomer(UUID.randomUUID()))
					.content(objectMapper.writeValueAsString(request)));

			perform.andExpectAll(
				status().isOk(),
				content().string(expectedMessage)
			);
		}

		@Test
		@DisplayName("[401]")
		@Disabled("인증/인가 필터 구현 후 테스트 활성화")
		void shouldReturn401(@Autowired MockMvc mockMvc, @Autowired ObjectMapper objectMapper) throws Exception {
			Long productId = 10L;
			int quantity = 2;
			Integer productVersion = 3;
			AddToCartRequest request = new AddToCartRequest(productId, quantity, productVersion);
			String expectedMessage = "";

			ResultActions perform = mockMvc.perform(
				post("/carts/my/addToCart", productId)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)));

			perform.andExpectAll(
				status().isUnauthorized(),
				content().string(expectedMessage)
			);
		}
	}
}
