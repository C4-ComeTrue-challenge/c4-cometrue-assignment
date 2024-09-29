package org.c4marathon.assignment.product.presentation;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.global.exception.AuthException;
import org.c4marathon.assignment.member.domain.Merchant;
import org.c4marathon.assignment.member.service.MerchantService;
import org.c4marathon.assignment.product.dto.request.CreateProductRequest;
import org.c4marathon.assignment.product.service.MerchantProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.c4marathon.assignment.global.exception.exceptioncode.ExceptionCode.NO_AUTHORITY;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/merchant/products")
@RequiredArgsConstructor
public class MerchantProductController {

    private final MerchantProductService productService;
    private final MerchantService merchantService;

    @PostMapping
    public ResponseEntity<Void> addProduct(
            @RequestBody CreateProductRequest request,
            Authentication authentication
    ) {
        checkMerchant(authentication);
        Merchant merchant = merchantService.findMerchantById(getMerchantId(authentication));
        productService.addProduct(merchant,
                                  request.productName(),
                                  request.description(),
                                  request.price(),
                                  request.stock());

        return ResponseEntity.status(CREATED).build();
    }

    private static long getMerchantId(Authentication authentication) {
        return Long.parseLong(authentication.getName());
    }

    private void checkMerchant(Authentication authentication) {
        if (!authentication.getAuthorities().toString().contains("MERCHANT")) {
            throw new AuthException(NO_AUTHORITY);
        }
    }
}
