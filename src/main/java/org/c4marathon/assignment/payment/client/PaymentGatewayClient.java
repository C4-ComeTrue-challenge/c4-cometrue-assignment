package org.c4marathon.assignment.payment.client;

public interface PaymentGatewayClient {
	String startPayment();

	String acceptPayment();

	String cancelPayment();

	String checkPayment();
}
