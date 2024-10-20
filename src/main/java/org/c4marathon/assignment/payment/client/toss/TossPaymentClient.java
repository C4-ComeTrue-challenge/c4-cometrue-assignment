package org.c4marathon.assignment.payment.client.toss;

import org.c4marathon.assignment.payment.client.PaymentGatewayClient;

public interface TossPaymentClient extends PaymentGatewayClient {
	/**
	 * <h3>결제 생성하기</h3>
	 * <p>결제 건을 생성합니다.</p>
	 *
	 * <h5>sample</h5>
	 * POST /api/v2/payments
	 * {
	 * "orderNo":"1",                                           # 토스몰 고유의 주문번호 (필수)
	 * "amount":25000,                                          # 결제 금액 (필수)
	 * "amountTaxFree":0,                                       # 비과세 금액 (필수)
	 * "productDesc":"토스티셔츠",                                # 상품 정보 (필수)
	 * "apiKey":"sk_test_w5lNQylNqa5lNQe013Nq",                 # 상점의 API Key (필수)
	 * "retUrl":"http://YOUR-SITE.COM/ORDER-CHECK?orderno=1",   # 결제 완료 후 연결할 웹 URL (필수)
	 * "retCancelUrl":"http://YOUR-SITE.COM/close",             # 결제 취소 시 연결할 웹 URL (필수)
	 * "autoExecute":true,                                      # 자동 승인 설정 (필수)
	 * "resultCallback":"https://YOUR-SITE.COM/callback",       # 결제 결과 callback 웹 URL (필수-자동승인설정 true의 경우)
	 * "callbackVersion":"V2",                                  # callback 버전 (필수-자동승인설정 true의 경우)
	 * "amountTaxable":22727,                                   # 결제 금액 중 과세금액
	 * "amountVat":2273,                                        # 결제 금액 중 부가세
	 * "amountServiceFee":0,                                    # 결제 금액 중 봉사료
	 * "expiredTime":"2019-06-17 12:47:35",                     # 결제 만료 예정 시각
	 * "cashReceipt":true,                                      # 현금영수증 발급 가능 여부
	 * }
	 *
	 * @return
	 * {
	 * "code":0,
	 * "checkoutPage":"https://pay.toss.im/payfront/auth?payToken=test_token1234567890",
	 * "payToken":"example-payToken"
	 * }
	 */
	String startPayment();

	/**
	 * <h3>승인하기</h3>
	 * <p>
	 *     결제 처리가 완료되면 결제 상태가 변경되고, 토스는 가맹점 서버로 변경 사항을 알려드립니다.
	 *     자동 승인 설정 시 (autoExecute가 true) 필수적으로 활용해야 합니다.
	 *     생성된 결제 건에 resultCallback 파라미터 값이 있을 경우에만 동작합니다.
	 * </p>
	 *
	 * POST YOUR-SITE.COM/callback (결제 생성 시 가맹점에서 설정한 callback URL)
	 * {
	 *  "status": "PAY_COMPLETE",
	 *  "payToken": "example-payToken",
	 *  "orderNo": "1",
	 *  "payMethod": "CARD",
	 *  "amount": 3000,
	 *  "discountedAmount": 600,
	 *  "paidAmount": 2300,
	 *  "paidTs": "2020-04-03 14:22:37",
	 *  "transactionId" : "dc3b951a-9781-462e-ab5a-b8a0bea0222a",
	 *  "cardCompanyCode": 3,
	 *  "cardAuthorizationNo": "87654321",
	 *  "spreadOut": 0,
	 *  "noInterest": false,
	 *  "cardMethodType": "CREDIT",
	 *  "cardUserType": "PERSONAL",
	 *  "cardNumber": "654321******1234",
	 *  "cardBinNumber": "654321",
	 *  "cardNum4Print": "1234",
	 *  "salesCheckLinkUrl": "https://pay.toss.im/payfront/web/external/sales-check?payToken=example-payToken&transactionId=2da1ca05-d91d-410f-976d-7a610242da8a",
	 *  //"paidPoint": 0, // 2020.08.06 이후 fadeout 된 레거시 포인트 금액으로 0원으로 나감
	 * }
	 *
	 * <p>
	 *     결제완료 callback을 받았을 때, 결제완료 상태를 변경하시고 재고차감 등의 로직을 처리해 주세요.
	 * </p>
	 *
	 * <h5>sample</h5>
	 * POST /api/v2/execute
	 * {
	 * "apiKey":"sk_test_w5lNQylNqa5lNQe013Nq", # 상점의 API Key (필수)
	 * "payToken":"example-payToken",           # 결제 고유 번호 (필수)
	 * }
	 *
	 * @return
	 * none
	 */
	String acceptPayment();

	/**
	 * <h3>환불하기</h3>
	 *
	 * <h5>sample</h5>
	 * POST /api/v2/refunds
	 * {
	 * "apiKey":"sk_test_w5lNQylNqa5lNQe013Nq", # 상점의 API Key (필수)
	 * "payToken":"example-payToken",           # 결제 고유 번호 (필수)
	 * "amount":10000,                          # 환불할 금액 (필수)
	 * "amountTaxable":5000,                    # 환불할 금액 중 과세금액
	 * "amountTaxFree":4000,                    # 환불할 금액 중 비과세금액 (필수)
	 * "amountVat":500,                         # 환불할 금액 중 부가세
	 * "amountServiceFee":500                   # 환불할 금액 중 봉사료
	 * }
	 *
	 * @return
	 * none
	 */
	String cancelPayment();

	/**
	 * <h3>결제 상태 알아보기</h3>
	 *
	 * <h5>sample</h5>
	 * POST /api/v2/status
	 * {
	 * "apiKey":"sk_test_w5lNQylNqa5lNQe013Nq",   # 상점의 API Key (필수)
	 * "payToken":"example-payToken",             # 결제 고유 번호 (필수)
	 * }
	 *
	 * @return
	 * {
	 * "code": 0,                            # 응답코드
	 * "payToken": "example-payToken",       # 결제 고유 토큰
	 * "orderNo": "1",                       # 상점의 주문번호
	 * "payStatus": "PAY_COMPLETE",          # 결제 상태
	 * "payMethod": "TOSS_MONEY",            # 결제 수단
	 * "amount": 15000,                      # 결제 요청금액
	 * "transactions": [                     # 거래 트랜잭션 목록
	 *  {
	 *     "stepType": "PAY",
	 *     "transactionId": "3243c76e-4669-881b-33a3b82ddf49",
	 *     "transactionAmount": 15000,
	 *     "discountAmount": 300,
	 *     "pointAmount": 0,
	 *     "paidAmount": 14700,
	 *     "regTs": "2020-03-01 12:33:20"
	 *   }
	 * ],
	 * "createdTs": "2020-03-01 12:33:04",   # 최초 결제요청 시간
	 * "paidTs": "2020-03-01 12:33:20"       # 결제 완료 처리 시간
	 * }
	 */
	String checkPayment();
}
