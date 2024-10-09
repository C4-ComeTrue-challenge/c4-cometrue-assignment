package org.c4marathon.assignment.auth.ui;

import org.c4marathon.assignment.auth.application.SignInService;
import org.c4marathon.assignment.auth.domain.vo.UserType;
import org.c4marathon.assignment.auth.ui.dto.request.SignInRequest;
import org.c4marathon.assignment.common.api.ApiResponse;
import org.c4marathon.assignment.common.authentication.model.Authentication;
import org.c4marathon.assignment.global.session.SessionConst;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class AuthController implements AuthApi {
	private final HttpServletRequest httpServletRequest;
	private final SignInService signInService;

	public AuthController(HttpServletRequest httpServletRequest, SignInService signInService) {
		this.httpServletRequest = httpServletRequest;
		this.signInService = signInService;
	}

	@Override
	public ResponseEntity<ApiResponse<Void>> postSignInCustomer(SignInRequest request) {
		SignInService.Command command = new SignInService.Command(request.email(), request.password(),
			UserType.CUSTOMER);
		Authentication authentication = signInService.signIn(command);
		storeInSession(authentication);
		return ResponseEntity.ok().build();
	}

	@Override
	public ResponseEntity<ApiResponse<Void>> postSignInSeller(SignInRequest request) {
		SignInService.Command command = new SignInService.Command(request.email(), request.password(),
			UserType.SELLER);
		Authentication authentication = signInService.signIn(command);
		storeInSession(authentication);
		return ResponseEntity.ok().build();
	}

	private void storeInSession(Authentication authentication) {
		httpServletRequest.getSession(true)
			.setAttribute(SessionConst.SESSION_AUTHENTICATION_PRINCIPAL_KEY, authentication);
		httpServletRequest.changeSessionId();
	}
}
