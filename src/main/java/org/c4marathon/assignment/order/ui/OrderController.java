package org.c4marathon.assignment.order.ui;

import java.util.UUID;

import org.c4marathon.assignment.common.api.ApiResponse;
import org.c4marathon.assignment.common.authentication.model.principal.LoginCustomer;
import org.c4marathon.assignment.order.application.AddToCartService;
import org.c4marathon.assignment.order.ui.dto.request.AddToCartRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController implements OrderApi {
	private final AddToCartService addToCartService;

	public OrderController(AddToCartService addToCartService) {
		this.addToCartService = addToCartService;
	}

	@Override
	public ResponseEntity<ApiResponse<Void>> postAddToCart(AddToCartRequest request, LoginCustomer loginCustomer) {
		AddToCartService.Command command = new AddToCartService.Command((UUID)loginCustomer.getId(),
			request.productId(), request.quantity(), request.version());
		addToCartService.add(command);
		return ResponseEntity.ok().build();
	}
}
