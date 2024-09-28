package org.c4marathon.assignment.seller.ui;

import java.net.URI;
import java.util.UUID;

import org.c4marathon.assignment.seller.application.SignUpSellerService;
import org.c4marathon.assignment.seller.ui.dto.request.SignUpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SellerController implements SellerApi {
	private final SignUpSellerService signUpSellerService;

	public SellerController(SignUpSellerService signUpSellerService) {
		this.signUpSellerService = signUpSellerService;
	}

	@Override
	public ResponseEntity<?> postSignUp(SignUpRequest request) {
		SignUpSellerService.Command command = new SignUpSellerService.Command(request.email(), request.password(),
			request.name(), request.licenseNumber());
		UUID id = signUpSellerService.register(command);
		return ResponseEntity.created(URI.create("/sellers/" + id)).build();
	}
}
