package org.c4marathon.assignment.product.presentation;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.product.dto.response.ProductPageResponse;
import org.c4marathon.assignment.product.service.CustomerProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class CustomerProductController {

    private final CustomerProductService customerProductService;

    @GetMapping
    public ResponseEntity<ProductPageResponse> getProductPage(
            @RequestParam(required = false) Long productCursorId,
            @RequestParam String searchKeyword,
            Authentication authentication
    ) {
        ProductPageResponse response = customerProductService.getProducts(productCursorId, searchKeyword);
        return ResponseEntity.ok(response);
    }
}
