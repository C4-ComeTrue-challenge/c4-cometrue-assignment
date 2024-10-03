package org.c4marathon.assignment.order.presentation;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.global.exception.AuthException;
import org.c4marathon.assignment.order.dto.OrderDto;
import org.c4marathon.assignment.order.dto.request.OrderRequest;
import org.c4marathon.assignment.order.service.OrderFacadeService;
import org.c4marathon.assignment.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.c4marathon.assignment.global.exception.exceptioncode.ExceptionCode.NO_AUTHORITY;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/orders/buy")
@RequiredArgsConstructor
public class OrderController {

    private final OrderFacadeService orderFacadeService;

    @PostMapping()
    public ResponseEntity<Void> order(
            @RequestBody OrderRequest orderRequest,
            Authentication authentication
    ) {
        validateCustomer(authentication);
        Long customerId = getCustomerId(authentication);
        orderFacadeService.buyProduct(new OrderDto(customerId,
                                                   orderRequest.merchantId(),
                                                   orderRequest.productId(),
                                                   orderRequest.quantity()));

        return ResponseEntity.status(CREATED).build();
    }

    private void validateCustomer(Authentication authentication) {
        if(!authentication.getAuthorities().toString().contains("CUSTOMER"))
            throw new AuthException(NO_AUTHORITY);
    }

    private Long getCustomerId(Authentication authentication) {
        return Long.parseLong(authentication.getName());
    }
}
