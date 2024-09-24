package org.c4marathon.assignment.user.ui;

import java.net.URI;
import java.util.UUID;

import org.c4marathon.assignment.user.application.SignUpService;
import org.c4marathon.assignment.user.ui.dto.request.SignUpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController implements AuthApi {
	private final SignUpService signUpService;

	public AuthController(SignUpService signUpService) {
		this.signUpService = signUpService;
	}

	@Override
	public ResponseEntity<?> postSignUp(SignUpRequest request) {
		SignUpService.Command command =
			new SignUpService.Command(request.email(), request.password(), request.name(), request.type());
		UUID id = signUpService.registerUser(command);
		return ResponseEntity.created(URI.create("/users/" + id)).build();
	}
}
