package org.c4marathon.assignment.payment.client.naver;

import org.springframework.web.service.annotation.PostExchange;

public interface NaverPayClient {
	/**
	 * <h3>단건결제 예약</h3>
	 * <p>
	 *     결제예약을 수행하고 결제예약 ID를 반환합니다.
	 *     해당 결제예약 ID를 이용하여 결제창을 호출합니다.
	 * </p>
	 *
	 * <h5>sample</h5>
	 * POST /{파트너 ID}/naverpay/payments/v2/reserve
	 * X-Naver-Client-Id:{발급된 client id}
	 * X-Naver-Client-Secret:{발급된 client secret}
	 * X-NaverPay-Chain-Id:{발급된 chain id}
	 * X-NaverPay-Idempotency-Key: {API 멱등성 키}
	 * {
	 *     "modelVersion": "2",
	 *     "merchantUserKey": "muserkey",
	 *     "merchantPayKey": "mpaykey",
	 *     "productName": "상품명",
	 *     "productCount": 10,
	 *     "totalPayAmount": 1000,
	 *     "returnUrl": "{your-returnUrl}",
	 *     "taxScopeAmount": 1000,
	 *     "taxExScopeAmount": 0,
	 *     "environmentDepositAmount": 0,
	 *     "purchaserName": "구매자이름",
	 *     "purchaserBirthday": "20000101",
	 *     "productItems": [{
	 *             "categoryType": "BOOK",
	 *             "categoryId": "GENERAL",
	 *             "uid": "107922211",
	 *             "name": "한국사",
	 *             "payReferrer": "NAVER_BOOK",
	 *             "count": 10
	 *     }, {
	 *             "categoryType": "MUSIC",
	 *             "categoryId": "CD",
	 *             "uid": "299911002",
	 *             "name": "Loves",
	 *             "payReferrer": "NAVER_BOOK",
	 *             "count": 10
	 *     }]
	 * }
	 *
	 * @return
	 * {
	 *    "code" : "Success",
	 *    "message" : "detail message(optional)",
	 *    "body" : {
	 *        "reserveId" : ""
	 *    }
	 * }
	 */
	@PostExchange("/{파트너 ID}/naverpay/payments/v2/reserve")
	String startPayment();

	/**
	 * <h3>결제 승인</h3>
	 * <p></p>
	 *
	 * <h5>sample</h5>
	 * POST /{파트너 ID}/naverpay/payments/v2.2/apply/payment
	 * X-Naver-Client-Id:{발급된 client id}
	 * X-Naver-Client-Secret:{발급된 client secret}
	 * X-NaverPay-Chain-Id:{발급된 chain id}
	 * X-NaverPay-Idempotency-Key: {API 멱등성 키}
	 * paymentId={네이버페이가 발급한 결제 번호}
	 *
	 * @return
	 *   {
	 *       "code" : "Success",
	 *       "message": "detail message(optional)",
	 *       "body": {
	 *           "paymentId": "20170201NP1043587746",
	 *           "detail": {
	 *                  "productName": "상품명",
	 *                  "merchantId": "loginId",
	 *                  "merchantName": "가맹점명",
	 *                  "cardNo": "465887**********",
	 *                  "admissionYmdt": "20170201151722",
	 *                  "payHistId": "20170201NP1043587781",
	 *                  "totalPayAmount": 1000,
	 *                  "applyPayAmount": 1000,
	 *                  "primaryPayAmount": 1000,
	 *                  "npointPayAmount": 0,
	 *                  "giftCardAmount": 0,
	 *                  "discountPayAmount": 0,
	 *                  "taxScopeAmount": 1000,
	 *                  "taxExScopeAmount": 0,
	 *                  "environmentDepositAmount": 0,
	 *                  "primaryPayMeans": "CARD",
	 *                  "merchantPayKey": "order-key",
	 *                  "merchantUserKey": "jenie",
	 *                  "cardCorpCode": "C0",
	 *                  "paymentId": "20170201NP1043587746",
	 *                  "admissionTypeCode": "01",
	 *                  "settleExpectAmount": 971,
	 *                  "payCommissionAmount": 29,
	 *                  "admissionState": "SUCCESS",
	 *                  "tradeConfirmYmdt": "20170201152510",
	 *                  "cardAuthNo": "17545616",
	 *                  "cardInstCount": 0,
	 *                  "usedCardPoint": false,
	 *                  "bankCorpCode": "",
	 *                  "bankAccountNo": "",
	 *                  "settleExpected": false,
	 *                  "extraDeduction": false,
	 *                  "useCfmYmdt" : "20180703",
	 *                  "userIdentifier": "t1GLYqqJ05MnViYdR/GdMDpzdocRclgTL4mBLn0R1Ls="
	 *           }
	 *       }
	 *   }
	 */
	String acceptPayment();

	/**
	 * <h3>결제 취소</h3>
	 * <p></p>
	 *
	 * <h5>sample</h5>
	 * POST /{파트너 ID}/naverpay/payments/v1/cancel
	 * X-Naver-Client-Id:{발급된 client id}
	 * X-Naver-Client-Secret:{발급된 client secret}
	 * X-NaverPay-Chain-Id:{발급된 chain id}
	 * X-NaverPay-Idempotency-Key: {API 멱등성 키}
	 * paymentId={결제 완료 시 전달된 paymentId}
	 * cancelAmount={취소금액}
	 * cancelReason=testCancel
	 * cancelRequester=2
	 *
	 * @return
	 */
	String cancelPayment();

	/**
	 * <h3>결제내역조회</h3>
	 * <p></p>
	 *
	 * <h5>sample</h5>
	 * GET/POST /{파트너 ID}/naverpay/payments/v2.2/list/history/{paymentId}
	 * X-Naver-Client-Id:{발급된 client id}
	 * X-Naver-Client-Secret:{발급된 client secret}
	 * X-NaverPay-Chain-Id:{발급된 chain id}
	 * {
	 *     "pageNumber": 1,
	 *     "rowsPerPage": 50
	 * }
	 *
	 * @return
	 * {
	 *     "code" : "Success",
	 *     "message": "detail message(optional)",
	 *     "body": {
	 *         "responseCount": 1,
	 *         "totalCount": 1,
	 *         "totalPageCount": 1,
	 *         "currentPageNumber": 1,
	 *         "list": [
	 *             {
	 *                 "cardAuthNo": "00000000",
	 *                 "bankAccountNo": "",
	 *                 "bankCorpCode": "",
	 *                 "paymentId": "20170000NP1000229665",
	 *                 "cardCorpCode": "C0",
	 *                 "cardInstCount": 0,
	 *                 "usedCardPoint": false,
	 *                 "settleInfo": {
	 *                     "primaryCommissionAmount": 30,
	 *                     "npointCommissionAmount": 20,
	 *                     "giftCardCommissionAmount": 0,
	 *                     "discountCommissionAmount": 0,
	 *                     "primarySettleAmount": 470,
	 *                     "npointSettleAmount": 480,
	 *                     "giftCardSettleAmount": 0,
	 *                     "discountSettleAmount": 0,
	 *                     "totalSettleAmount": 950,
	 *                     "totalCommissionAmount": 50,
	 *                     "settleCreated": true
	 *                 },
	 *                 "merchantName": "나의가맹점",
	 *                 "productName": "나의상품",
	 *                 "payHistId": "20170000NP1000229668",
	 *                 "merchantId": "MID12345",
	 *                 "admissionYmdt": "20170914163930",
	 *                 "tradeConfirmYmdt": "20170915163956",
	 *                 "totalPayAmount": 1000,
	 *                 "applyPayAmount": 1000,
	 *                 "merchantPayKey": "orderKey-91516397",
	 *                 "merchantUserKey": "ID12345",
	 *                 "admissionTypeCode": "01",
	 *                 "primaryPayMeans": "CARD",
	 *                 "admissionState": "SUCCESS",
	 *                 "primaryPayAmount": 500,
	 *                 "npointPayAmount": 500,
	 *                 "giftCardPayAmount": 0,
	 *                 "discountPayAmount": 0,
	 *                 "taxScopeAmount": 1000,
	 *                 "taxExScopeAmount": 0,
	 *                 "environmentDepositAmount": 0,
	 *                 "cardNo": "123456**********",
	 *                 "extraDeduction": false,
	 *                 "useCfmYmdt" : "20180703",
	 *                 "merchantExtraParameter" : "testExtraParameter"
	 *            }
	 *        ]
	 *     }
	 * }
	 */
	String checkPayment();
}
