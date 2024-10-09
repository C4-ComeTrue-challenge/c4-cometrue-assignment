package org.c4marathon.assignment.auth.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import org.c4marathon.assignment.AcceptanceTest;
import org.c4marathon.assignment.auth.ui.dto.request.SignInRequest;
import org.c4marathon.assignment.common.authentication.model.Authentication;
import org.c4marathon.assignment.common.authentication.model.principal.LoginCustomer;
import org.c4marathon.assignment.common.authentication.model.principal.LoginSeller;
import org.c4marathon.assignment.common.authentication.model.principal.Principal;
import org.c4marathon.assignment.common.entity.Point;
import org.c4marathon.assignment.customer.infra.persist.CustomerRecord;
import org.c4marathon.assignment.customer.infra.persist.datajpa.JpaCustomerRecordRepository;
import org.c4marathon.assignment.global.session.SessionConst;
import org.c4marathon.assignment.seller.infra.persist.SellerRecord;
import org.c4marathon.assignment.seller.infra.persist.datajpa.JpaSellerRecordRepository;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

class AuthApiTest extends AcceptanceTest {
	@Autowired
	private JpaCustomerRecordRepository customerRepository;
	@Autowired
	private JpaSellerRecordRepository sellerRepository;

	@BeforeEach
	void setUp() {
		customerRepository.save(
			new CustomerRecord(UUID.randomUUID(), "customer@email.com", "{noop}customerPassword", "customerName",
				new Point()));
		sellerRepository.save(
			new SellerRecord(UUID.randomUUID(), "seller@email.com", "{noop}sellerPassword", "customerName",
				"000-0000-000", new Point()));
	}

	@AfterEach
	void tearDown() {
		customerRepository.deleteAll();
		sellerRepository.deleteAll();
	}

	@Nested
	@DisplayName("POST /sign-in/customer")
	class SignInCustomer {
		@Test
		@DisplayName("[200]")
		void shouldReturn200(@Autowired MockMvc mockMvc, @Autowired ObjectMapper objectMapper) throws Exception {
			SignInRequest request = new SignInRequest("customer@email.com", "customerPassword");
			String expectedMessage = "";

			ResultActions perform = mockMvc.perform(
				post("/sign-in/customer")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)));

			perform.andExpectAll(
				status().isOk(),
				request().sessionAttribute(SessionConst.SESSION_AUTHENTICATION_PRINCIPAL_KEY,
					isPrincipal(LoginCustomer.class)),
				content().string(expectedMessage)
			);
		}
	}

	@Nested
	@DisplayName("POST /sign-in/seller")
	class SignInSeller {
		@Test
		@DisplayName("[200]")
		void shouldReturn200(@Autowired MockMvc mockMvc, @Autowired ObjectMapper objectMapper) throws Exception {
			SignInRequest request = new SignInRequest("seller@email.com", "sellerPassword");
			String expectedMessage = "";

			ResultActions perform = mockMvc.perform(
				post("/sign-in/seller")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)));

			perform.andExpectAll(
				status().isOk(),
				request().sessionAttribute(SessionConst.SESSION_AUTHENTICATION_PRINCIPAL_KEY,
					isPrincipal(LoginSeller.class)),
				content().string(expectedMessage)
			);
		}
	}

	private Matcher<Object> isPrincipal(Class<? extends Principal> principalClazz) {
		return Matchers.allOf(
			Matchers.instanceOf(Authentication.class),
			Matchers.hasProperty("principal", Matchers.instanceOf(principalClazz))
		);
	}
}
