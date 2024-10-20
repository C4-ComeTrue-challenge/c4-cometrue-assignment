package org.c4marathon.assignment.customer.ui;

import java.net.URI;
import java.util.UUID;

import org.c4marathon.assignment.common.api.ApiResponse;
import org.c4marathon.assignment.customer.application.ChargePointService;
import org.c4marathon.assignment.customer.application.SignUpService;
import org.c4marathon.assignment.customer.ui.dto.request.SignUpRequest;
import org.c4marathon.assignment.customer.ui.dto.response.ChargePointResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController implements CustomerApi {
	private final SignUpService signUpService;
	private final ChargePointService chargePointService;

	public CustomerController(SignUpService signUpService, ChargePointService chargePointService) {
		this.signUpService = signUpService;
		this.chargePointService = chargePointService;
	}

	@Override
	public ResponseEntity<ApiResponse<Void>> postSignUp(SignUpRequest request) {
		SignUpService.Command command = new SignUpService.Command(request.email(), request.password(), request.name());
		UUID id = signUpService.register(command);
		return ResponseEntity.created(URI.create("/customers/" + id)).build();
	}

	@Override
	public ResponseEntity<ApiResponse<ChargePointResponse>> postChargePoint(ChargePointRequest request) {
		ChargePointService.Command command = new ChargePointService.Command(request.amount(), request.paymentGateway());
		String checkoutPage = chargePointService.startPayment(command);
		return ResponseEntity.ok(
			new ApiResponse<>(200, "success", new ChargePointResponse(checkoutPage))
		);
	}
}
