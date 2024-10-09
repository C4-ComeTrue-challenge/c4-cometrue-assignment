package org.c4marathon.assignment.product.ui;

import org.c4marathon.assignment.common.api.ApiResponse;
import org.c4marathon.assignment.common.authentication.annotation.AuthenticationPrincipal;
import org.c4marathon.assignment.common.authentication.model.principal.LoginSeller;
import org.c4marathon.assignment.product.ui.dto.request.RegisterProductRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface ProductApi {
	@PostMapping("/products")
	ResponseEntity<ApiResponse<Void>> postRegister(@RequestBody RegisterProductRequest request,
												   @AuthenticationPrincipal LoginSeller loginSeller);
}
