package org.c4marathon.assignment.user.ui;

import org.c4marathon.assignment.user.ui.dto.request.SignUpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthApi {
	@PostMapping("/sign-up")
	ResponseEntity<?> postSignUp(@RequestBody SignUpRequest request);
}
