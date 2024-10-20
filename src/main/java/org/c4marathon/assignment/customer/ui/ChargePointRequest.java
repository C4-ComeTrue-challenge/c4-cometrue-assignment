package org.c4marathon.assignment.customer.ui;

public record ChargePointRequest(
	Integer amount,
	String paymentGateway
) {
}
