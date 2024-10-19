package org.c4marathon.assignment.product.ui;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import org.c4marathon.assignment.common.authentication.model.principal.LoginSeller;
import org.c4marathon.assignment.global.session.SessionConst;
import org.c4marathon.assignment.product.application.RegisterProductService;
import org.c4marathon.assignment.product.ui.dto.request.RegisterProductRequest;
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

@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {
	@MockBean
	RegisterProductService registerProductService;

	@Nested
	@DisplayName("POST /products")
	class SignUp {
		@Test
		@DisplayName("[201]")
		void shouldReturn201(@Autowired MockMvc mockMvc, @Autowired ObjectMapper objectMapper) throws Exception {
			UUID sellerId = UUID.randomUUID();
			Long nextProductId = 10L;

			RegisterProductRequest request = new RegisterProductRequest("newProduct", "this is new product", 10000L,
				20L);
			String expectedMessage = "";

			when(registerProductService.register(
				new RegisterProductService.Command(sellerId, request.name(), request.description(), request.price(),
					request.stock())))
				.thenReturn(nextProductId);

			ResultActions perform = mockMvc.perform(
				post("/products")
					.contentType(MediaType.APPLICATION_JSON)
					.sessionAttr(SessionConst.SESSION_AUTHENTICATION_PRINCIPAL_KEY, new LoginSeller(sellerId))
					.content(objectMapper.writeValueAsString(request)));

			perform.andExpectAll(
				status().isCreated(),
				header().string("Location", "/products/" + nextProductId),
				content().string(expectedMessage)
			);
			verify(registerProductService).register(
				new RegisterProductService.Command(sellerId, request.name(), request.description(), request.price(),
					request.stock()));
		}
	}
}
