package org.c4marathon.assignment.product.ui;

import java.net.URI;
import java.util.UUID;

import org.c4marathon.assignment.common.api.ApiResponse;
import org.c4marathon.assignment.common.authentication.model.principal.LoginSeller;
import org.c4marathon.assignment.product.application.RegisterProductService;
import org.c4marathon.assignment.product.ui.dto.request.RegisterProductRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController implements ProductApi {
	private final RegisterProductService registerProductService;

	public ProductController(RegisterProductService registerProductService) {
		this.registerProductService = registerProductService;
	}

	@Override
	public ResponseEntity<ApiResponse<Void>> postRegister(RegisterProductRequest request, LoginSeller loginSeller) {
		RegisterProductService.Command command = new RegisterProductService.Command((UUID)loginSeller.getId(),
			request.name(), request.description(), request.price(), request.stock());
		Long id = registerProductService.register(command);
		return ResponseEntity.created(URI.create("/products/" + id)).build();
	}
}
