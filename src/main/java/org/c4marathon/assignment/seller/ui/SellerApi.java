package org.c4marathon.assignment.seller.ui;

import org.c4marathon.assignment.seller.ui.dto.request.SignUpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface SellerApi {
	@PostMapping("/sellers")
	ResponseEntity<?> postSignUp(@RequestBody SignUpRequest request);
}
