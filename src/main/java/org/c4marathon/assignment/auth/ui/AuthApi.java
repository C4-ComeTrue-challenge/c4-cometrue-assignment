package org.c4marathon.assignment.auth.ui;

import org.c4marathon.assignment.auth.ui.dto.request.SignInRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthApi {
	@PostMapping("/sign-in/customer")
	ResponseEntity<?> postSignInCustomer(@RequestBody SignInRequest request);

	@PostMapping("/sign-in/seller")
	ResponseEntity<?> postSignInSeller(@RequestBody SignInRequest request);
}
