package org.c4marathon.assignment.payment.client.kakao.dto.response;

import java.time.LocalDateTime;

public record StartPaymentResponse(
	String tid,
	String next_redirect_app_url,
	String next_redirect_mobile_url,
	String next_redirect_pc_url,
	String android_app_scheme,
	String ios_app_scheme,
	LocalDateTime created_at
) {
}
