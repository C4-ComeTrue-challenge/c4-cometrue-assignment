package org.c4marathon.assignment.order.domain;

/**
 * PENDING : 고객이 주문을 넣었지만 아직 결제는 하지 않은 상태
 * PROCESSING : 고객이 주문을 넣고 결제까지 완료한 상태(이 때, 재고랑 캐시는 차감됨)
 * FINISHED : 판매자가 주문을 승낙한 상태
 * CANCEL : 구매자가 결제를 넣고 취소한 상태
 * FAILED : 잔고가 부족하거나 재고가 부족해서 실패한 상태, 의도가 아닌 취소
 */
public enum OrderState {
    PENDING, PROCESSING, FINISHED, FAILED, CANCEL
}
