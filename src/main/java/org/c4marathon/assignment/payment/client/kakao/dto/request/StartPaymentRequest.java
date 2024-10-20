package org.c4marathon.assignment.payment.client.kakao.dto.request;

import java.util.List;
import java.util.Map;

/**
 * {
 * "cid": "TC0ONETIME",
 * "partner_order_id": "partner_order_id",
 * "partner_user_id": "partner_user_id",
 * "item_name": "초코파이",
 * "quantity": "1",
 * "total_amount": "2200",
 * "vat_amount": "200",
 * "tax_free_amount": "0",
 * "approval_url": "https://developers.kakao.com/success",
 * "fail_url": "https://developers.kakao.com/fail",
 * "cancel_url": "https://developers.kakao.com/cancel"
 * }
 */
public record StartPaymentRequest(
	/* 가맹점 코드, 10자 */
	String cid,
	/* 가맹점 코드 인증키, 24자, 숫자와 영문 소문자 조합 */
	String cidSecret,
	String partnerOrderId, // 가맹점 주문번호, 최대 100자
	String partnerUserId, // 가맹점 회원 id, 최대 100자 (실명, ID와 같은 개인정보가 포함되지 않도록 유의)
	String itemName, // 상품명, 최대 100자
	String itemCode, // 상품코드, 최대 100자
	Integer quantity, // 상품 수량
	Integer totalAmount, // 상품 총액
	Integer taxFreeAmount, // 상품 비과세 금액
	Integer vatAmount, // 상품 부가세 금액. 값을 보내지 않을 경우 다음과 같이 VAT 자동 계산 (상품총액 - 상품 비과세 금액)/11 : 소수점 이하 반올림
	Integer greenDeposit, // 컵 보증금
	String approvalUrl, // 결제 성공 시 redirect url, 최대 255자
	String cancelUrl, // 결제 취소 시 redirect url, 최대 255자
	String failUrl, // 결제 실패 시 redirect url, 최대 255자
	List<String> availableCards,
	// 결제 수단으로써 사용 허가할 카드사를 지정해야 하는 경우 사용. 카카오페이와 사전 협의 필요. 사용 허가할 카드사 코드*의 배열. ex) ["HANA", "BC"]. (기본값: 모든 카드사 허용)
	String paymentMethodType, // 사용 허가할 결제 수단, 지정하지 않으면 모든 결제 수단 허용. CARD 또는 MONEY 중 하나
	Integer installMonth, // 카드 할부개월, 0~12
	String useShareInstallment, // 분담무이자 설정 (Y/N), 사용 시 사전 협의 필요
	Map<String, String> customJson
	// 사전에 정의된 기능. 1. 결제 화면에 보여줄 사용자 정의 문구, 카카오페이와 사전 협의 필요. 2. iOS에서 사용자 인증 완료 후 가맹점 앱으로 자동 전환 기능(iOS만 처리가능, 안드로이드 동작불가). ex) return_custom_url과 함께 key 정보에 앱스킴을 넣어서 전송. "return_custom_url":"kakaotalk://"
) {
	public static StartPaymentRequest of(String cid, String partnerOrderId, String partnerUserId,
										 String itemName, Integer quantity, Integer totalAmount,
										 Integer taxFreeAmount, String approvalUrl,
										 String cancelUrl, String failUrl) {
		return new StartPaymentRequest(cid, null, partnerOrderId, partnerUserId, itemName, null, quantity, totalAmount,
			taxFreeAmount, null, null, approvalUrl, cancelUrl, failUrl, null, null, null, null, null);
	}
}
