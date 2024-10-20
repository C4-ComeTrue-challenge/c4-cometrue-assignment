package org.c4marathon.assignment.global.config.payclient;

import org.c4marathon.assignment.payment.client.kakao.KakaoPayClient;
import org.c4marathon.assignment.payment.client.naver.NaverPayClient;
import org.c4marathon.assignment.payment.client.toss.TossPaymentClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class PayClientConfig {
	private final PayClientProperty payClientProperty;

	public PayClientConfig(PayClientProperty payClientProperty) {
		this.payClientProperty = payClientProperty;
	}

	@Bean
	public NaverPayClient naverPayClient(RestClient.Builder clientBuilder) {
		PayClientProperty.NaverPayClientProperty properties = payClientProperty.getNaver();
		RestClient restClient = clientBuilder
			.baseUrl("https://dev.apis.naver.com")
			.defaultHeader("X-Naver-Client-Id", properties.getClientSecret())
			.defaultHeader("X-Naver-Client-Secret", properties.getClientSecret())
			.defaultHeader("X-NaverPay-Chain-Id", properties.getChainId())
			.build();
		HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
			.builderFor(RestClientAdapter.create(restClient))
			.build();
		return httpServiceProxyFactory.createClient(NaverPayClient.class);
	}

	@Bean
	public KakaoPayClient kakaoPayClient(RestClient.Builder clientBuilder) {
		PayClientProperty.KakaoPayClientProperty properties = payClientProperty.getKakao();
		RestClient restClient = clientBuilder
			.baseUrl("https://open-api.kakaopay.com")
			.defaultHeader("Authorization", properties.getSecretKey())
			.build();
		HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
			.builderFor(RestClientAdapter.create(restClient))
			.build();
		return httpServiceProxyFactory.createClient(KakaoPayClient.class);
	}

	@Bean
	public TossPaymentClient tossPaymentClient(RestClient.Builder clientBuilder) {
		RestClient restClient = clientBuilder
			.baseUrl("https://pay.toss.im")
			.build();
		HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
			.builderFor(RestClientAdapter.create(restClient))
			.build();
		return httpServiceProxyFactory.createClient(TossPaymentClient.class);
	}
}
