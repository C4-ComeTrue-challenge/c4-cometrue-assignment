package org.c4marathon.assignment.user.ui;

import org.c4marathon.assignment.user.ui.dto.request.SignUpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController implements AuthApi {
	@Override
	public ResponseEntity<?> postSignUp(SignUpRequest request) {
		throw new UnsupportedOperationException();
	}
}
