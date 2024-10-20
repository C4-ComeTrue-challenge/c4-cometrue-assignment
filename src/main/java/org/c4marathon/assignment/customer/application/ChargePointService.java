package org.c4marathon.assignment.customer.application;

import org.springframework.stereotype.Service;

@Service
public class ChargePointService {
	public String startPayment(Command cmd) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public record Command(
		Integer amount,
		String paymentGateway
	) {
	}
}
