package org.c4marathon.assignment.order.ui;

import org.c4marathon.assignment.common.authentication.annotation.AuthenticationPrincipal;
import org.c4marathon.assignment.common.authentication.model.principal.LoginCustomer;
import org.c4marathon.assignment.order.ui.dto.request.AddToCartRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface OrderApi {
	@PostMapping("/carts/my/addToCart")
	ResponseEntity<?> postAddToCart(@RequestBody AddToCartRequest request,
									@AuthenticationPrincipal LoginCustomer loginCustomer);
}
