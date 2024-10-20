package org.c4marathon.assignment.payment.client.kakao;

import org.c4marathon.assignment.payment.client.kakao.dto.request.StartPaymentRequest;
import org.c4marathon.assignment.payment.client.kakao.dto.response.StartPaymentResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface KakaoPayClient {
	/**
	 * <h3>결제준비(ready)</h3>
	 * <p>카카오페이 결제를 시작하기 위해 결제정보를 카카오페이 서버에 전달하고 결제 고유번호(TID)와 URL을 응답받는 단계입니다.</p>
	 * <ol>
	 *     <li>Secret key를 헤더에 담아 파라미터 값들과 함께 POST로 요청합니다.</li>
	 *     <li>요청이 성공하면 응답 바디에 JSON 객체로 다음 단계 진행을 위한 값들을 받습니다.</li>
	 *     <li>서버(Server)는 tid를 저장하고, 클라이언트는 사용자 환경에 맞는 URL로 리다이렉트(redirect)합니다.</li>
	 * </ol>
	 *
	 * <h5>sample</h5>
	 * POST /online/v1/payment/ready HTTP/1.1
	 * Authorization: SECRET_KEY ${SECRET_KEY}
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
	 *
	 * @return
	 * {
	 * "tid": "T1234567890123456789",
	 * "next_redirect_app_url": "https://mockup-pg-web.kakao.com/v1/xxxxxxxxxx/aInfo",
	 * "next_redirect_mobile_url": "https://mockup-pg-web.kakao.com/v1/xxxxxxxxxx/mInfo",
	 * "next_redirect_pc_url": "https://mockup-pg-web.kakao.com/v1/xxxxxxxxxx/info",
	 * "android_app_scheme": "kakaotalk://kakaopay/pg?url=https://mockup-pg-web.kakao.com/v1/xxxxxxxxxx/order",
	 * "ios_app_scheme": "kakaotalk://kakaopay/pg?url=https://mockup-pg-web.kakao.com/v1/xxxxxxxxxx/order",
	 * "created_at": "2023-07-15T21:18:22"
	 * }
	 */
	@PostExchange("/online/v1/payment/ready")
	StartPaymentResponse startPayment(@RequestBody StartPaymentRequest request);

	/**
	 * <h3>결제승인(approve)</h3>
	 * <p>사용자가 결제 수단을 선택하고 비밀번호를 입력해 결제 인증을 완료한 뒤, 최종적으로 결제 완료 처리를 하는 단계입니다.</p>
	 * <ul>
	 *     <li>인증완료 시 응답받은 pg_token과 tid로 최종 승인요청합니다.</li>
	 *     <li>결제 승인 API를 호출하면 결제 준비 단계에서 시작된 결제 건이 승인으로 완료 처리됩니다.</li>
	 *     <li>결제 승인 요청이 실패하면 카드사 등 결제 수단의 실패 정보가 필요에 따라 포함될 수 있습니다.</li>
	 * </ul>
	 *
	 * <h5>sample</h5>
	 * POST /online/v1/payment/approve HTTP/1.1
	 * {
	 * "cid": "TC0ONETIME",
	 * "tid": "T1234567890123456789",
	 * "partner_order_id": "partner_order_id",
	 * "partner_user_id": "partner_user_id",
	 * "pg_token": "xxxxxxxxxxxxxxxxxxxx"
	 * }
	 *
	 * @return
	 * 결제 수단 : Money
	 * {
	 *   "aid": "A5678901234567890123",
	 *   "tid": "T1234567890123456789",
	 *   "cid": "TC0ONETIME",
	 *   "partner_order_id": "partner_order_id",
	 *   "partner_user_id": "partner_user_id",
	 *   "payment_method_type": "MONEY",
	 *   "item_name": "초코파이",
	 *   "quantity": 1,
	 *   "amount": {
	 *     "total": 2200,
	 *     "tax_free": 0,
	 *     "vat": 200,
	 *     "point": 0,
	 *     "discount": 0,
	 *     "green_deposit": 0
	 *   },
	 *   "created_at": "2023-07-15T21:18:22",
	 *   "approved_at": "2023-07-15T21:18:22"
	 * }
	 * 결제 수단 : Card
	 * {
	 *   "cid": "TC0ONETIME",
	 *   "aid": "A5678901234567890123",
	 *   "tid": "T1234567890123456789",
	 *   "partner_user_id": "partner_user_id",
	 *   "partner_order_id": "partner_order_id",
	 *   "payment_method_type": "CARD",
	 *   "item_name": "초코파이",
	 *   "quantity": 1,
	 *   "amount": {
	 *     "total": 2200,
	 *     "tax_free": 0,
	 *     "vat": 200,
	 *     "discount": 0,
	 *     "point": 0,
	 *     "green_deposit": 0
	 *   },
	 *   "card_info": {
	 *     "interest_free_install": "N",
	 *     "bin": "621640",
	 *     "card_type": "체크",
	 *     "card_mid": "123456789",
	 *     "approved_id": "12345678",
	 *     "install_month": "00",
	 *     "installment_type": "CARD_INSTALLMENT",
	 *     "kakaopay_purchase_corp": "비씨카드",
	 *     "kakaopay_purchase_corp_code": "104",
	 *     "kakaopay_issuer_corp": "수협은행",
	 *     "kakaopay_issuer_corp_code": "212"
	 *   },
	 *   "created_at": "2023-07-15T21:18:22",
	 *   "approved_at": "2023-07-15T21:18:22"
	 * }
	 */
	String acceptPayment();

	/**
	 * <h3>결제 취소</h3>
	 * <p>
	 *     결제 고유번호인 tid에 해당하는 결제 건에 대해 지정한 금액만큼 결제 취소를 요청합니다.
	 *     단건 결제, 정기 결제 거래건에 대해 취소가 가능합니다.
	 *     전체 취소, 부분 취소 모두 가능하지만, 일부 특정 업종에 대해 부분 취소가 불가할 수 있습니다.
	 *     취소 요청 시 비과세(tax_free_amount)와 부가세(vat_amount)를 맞게 요청해주셔야 합니다.
	 * </p>
	 *
	 * <h5>sample</h5>
	 * POST /online/v1/payment/cancel HTTP/1.1
	 * Authorization: SECRET_KEY ${SECRET_KEY}
	 * {
	 * "cid": "TC0ONETIME",
	 * "tid": "T1234567890123456789",
	 * "cancel_amount": 2200,
	 * "cancel_tax_free_amount": 0,
	 * "cancel_vat_amount": 200,
	 * "cancel_available_amount": 2200,
	 * }
	 *
	 * @return
	 * {
	 *  "tid": "T1234567890123456789",
	 *  "cid": "TC0ONETIME",
	 *  "status": "CANCEL_PAYMENT",
	 *  "partner_order_id": "partner_order_id",
	 *  "partner_user_id": "partner_user_id",
	 *  "payment_method_type": "MONEY",
	 *  "item_name": "초코파이",
	 *  "quantity": 1,
	 *  "amount": {
	 *     "total": 2200,
	 *     "tax_free": 0,
	 *     "vat": 200,
	 *     "point": 0,
	 *     "discount": 0,
	 *     "green_deposit": 0
	 *   },
	 *  "amount": {
	 *         "total": 2200,
	 *         "tax_free": 0,
	 *         "vat": 200,
	 *         "point": 0,
	 *         "discount": 0,
	 *         "green_deposit": 0
	 *  },
	 *  "approved_cancel_amount": {
	 *         "total": 2200,
	 *         "tax_free": 0,
	 *         "vat": 200,
	 *         "point": 0,
	 *         "discount": 0,
	 *         "green_deposit": 0
	 *  },
	 *  "canceled_amount": {
	 *         "total": 2200,
	 *         "tax_free": 0,
	 *         "vat": 200,
	 *         "point": 0,
	 *         "discount": 0,
	 *         "green_deposit": 0
	 *  },
	 *  "cancel_available_amount": {
	 *         "total": 0,
	 *         "tax_free": 0,
	 *         "vat": 0,
	 *         "point": 0,
	 *         "discount": 0,
	 *         "green_deposit": 0
	 *  },
	 *  "created_at": "2023-07-15T21:18:22",
	 *  "approved_at": "2023-07-15T21:18:22",
	 *  "canceled_at": "2023-07-15T21:18:22"
	 * }
	 */
	String cancelPayment();

	/**
	 * <h3>주문 조회</h3>
	 *
	 * <h5>sample</h5>
	 * POST /online/v1/payment/order HTTP/1.1
	 * Authorization: SECRET_KEY ${SECRET_KEY}
	 * {
	 * "cid": "TC0ONETIME",
	 * "tid": "T1234567890123456789"
	 * }
	 *
	 * @return
	 * {
	 *  "tid": "T1234567890123456789",
	 *  "cid": "TC0ONETIME",
	 *  "status": "SUCCESS_PAYMENT",
	 *  "partner_order_id": "partner_order_id",
	 *  "partner_user_id": "partner_user_id",
	 *  "payment_method_type": "MONEY",
	 *  "item_name": "초코파이",
	 *  "quantity": 1,
	 *  "amount": {
	 *     "total": 2200,
	 *     "tax_free": 0,
	 *     "vat": 200,
	 *     "point": 0,
	 *     "discount": 0,
	 *     "green_deposit": 0
	 *   },
	 *  "canceled_amount": {
	 *         "total": 0,
	 *         "tax_free": 0,
	 *         "vat": 0,
	 *         "point": 0,
	 *         "discount": 0,
	 *         "green_deposit": 0
	 *  },
	 *  "cancel_available_amount": {
	 *         "total": 0,
	 *         "tax_free": 0,
	 *         "vat": 0,
	 *         "point": 0,
	 *         "discount": 0,
	 *         "green_deposit": 0
	 *  },
	 *  "created_at": "2023-07-15T21:18:22",
	 *  "approved_at": "2023-07-15T21:18:22",
	 *  "payment_action_details": [
	 *     {
	 *             "aid": "A5678901234567890123",
	 *             "payment_action_type": "PAYMENT",
	 *             "payment_method_type": "MONEY",
	 *             "amount": 2200,
	 *             "point_amount": 0,
	 *             "discount_amount": 0,
	 *             "approved_at": "2023-07-15T21:18:22",
	 *             "green_deposit": 0
	 *     }
	 *  ]
	 * }
	 */
	String checkPayment();
}
