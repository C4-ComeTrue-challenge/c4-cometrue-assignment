package org.c4marathon.assignment.global.config.payclient;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "payclient")
@Getter
@Setter
public class PayClientProperty {
	private NaverPayClientProperty naver;
	private KakaoPayClientProperty kakao;
	private TossPayClientProperty toss;

	@Getter
	@Setter
	public static class NaverPayClientProperty {
		private String clientId;
		private String clientSecret;
		private String chainId;
	}

	@Getter
	@Setter
	public static class KakaoPayClientProperty {
		private String cid;
		private String secretKey;
	}

	@Getter
	@Setter
	public static class TossPayClientProperty {
		private String clientId;
		private String clientSecret;
	}
}
