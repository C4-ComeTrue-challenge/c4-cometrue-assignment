package org.c4marathon.assignment.domin.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.c4marathon.assignment.domin.item.controller.ItemErrorStatus;
import org.c4marathon.assignment.domin.item.entity.Item;
import org.c4marathon.assignment.domin.item.repository.ItemRepository;
import org.c4marathon.assignment.domin.order.controller.OrderErrorStatus;
import org.c4marathon.assignment.domin.order.dto.OrderRequestDTO;
import org.c4marathon.assignment.domin.order.entity.Order;
import org.c4marathon.assignment.domin.order.entity.OrderItem;
import org.c4marathon.assignment.domin.order.entity.Status;
import org.c4marathon.assignment.domin.order.repository.OrderRepository;
import org.c4marathon.assignment.domin.user.controller.UserErrorStatus;
import org.c4marathon.assignment.domin.user.entity.User;
import org.c4marathon.assignment.domin.user.repository.UserRepository;
import org.c4marathon.assignment.global.exception.GeneralException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createOrder(OrderRequestDTO.CreateOrderDTO createOrderDTO, Long userId) {
        User user = userRepository.findByIdWithPessimisticLock(userId)
                .orElseThrow(() -> new GeneralException(UserErrorStatus.USER_INFO_NOT_FOUND));
        List<OrderItem> orderItems = createOrderDTO.getOrderItems().stream()
                .map(orderItem -> {
                    Item item = itemRepository.findByIdWithPessimisticLock(orderItem.getItemId()) // 비관적 락 적용
                            .orElseThrow(() -> new GeneralException(ItemErrorStatus.ITEM_INFO_NOT_FOUND));

                    if (item.getStock() < orderItem.getQuantity()) {
                        throw new GeneralException(OrderErrorStatus.INVALID_CREAT_ORDER);
                    }

                    item.decreaseStock(orderItem.getQuantity());

                    return OrderItem.builder()
                            .item(item)
                            .orderPrice(item.getPrice())
                            .orderQuantity(orderItem.getQuantity())
                            .build();

                })
                .collect(Collectors.toList());
        // 총 주문 금액 계산
        int totalPrice = orderItems.stream()
                .mapToInt(orderItem -> orderItem.getOrderPrice() * orderItem.getOrderQuantity())
                .sum();

        if (user.getCache() < totalPrice) {
            throw new GeneralException(OrderErrorStatus.REJECT_PAYMENT);
        }
        user.decreaseCache(totalPrice);


        Order order = Order.builder()
                .user(user)
                .orderItems(orderItems)
                .orderStatus(Status.주문_완료)
                .totalOrderPrice(totalPrice)
                .build();

        orderRepository.save(order);
    }

    @Transactional
    public void refundOrder(Long orderId, Long userId) {
        User user = userRepository.findByIdWithPessimisticLock(userId)
                .orElseThrow(() -> new GeneralException(UserErrorStatus.USER_INFO_NOT_FOUND));

        Order order = orderRepository.findByIdWithPessimisticLock(orderId)
                .orElseThrow(() -> new GeneralException(OrderErrorStatus.NOT_FOUND_ORDER));

        if (order.getOrderStatus() != Status.주문_완료) {
            throw new GeneralException(OrderErrorStatus.INVALID_ORDER_STATUS);
        }

        if (!order.getUser().getId().equals(userId)) {
            throw new GeneralException(OrderErrorStatus.UNAUTHORIZED_ACCESS);
        }

        order.getOrderItems().forEach(orderItem -> {
            Item item = itemRepository.findByIdWithPessimisticLock(orderItem.getItem().getId())
                    .orElseThrow(() -> new GeneralException(ItemErrorStatus.ITEM_INFO_NOT_FOUND));
            item.increaseStock(orderItem.getOrderQuantity());
        });

        order.updateStatus(Status.취소);
        user.increaseCache(order.getTotalOrderPrice());
    }

    @Transactional
    public void confirmOrder(Long orderId) {
        Order order = orderRepository.findByIdWithPessimisticLock(orderId)
                .orElseThrow(() -> new GeneralException(OrderErrorStatus.NOT_FOUND_ORDER));

        // 판매자 캐시 업데이트
        Map<Long, Integer> sellerCacheUpdateMap = new HashMap<>();
        order.getOrderItems().forEach(orderItem -> {
            Item item = orderItem.getItem();
            Long sellerId = item.getUser().getId();
            int cacheToUpdate = (int) (orderItem.getOrderPrice() * orderItem.getOrderQuantity() * 0.95);

            sellerCacheUpdateMap.put(sellerId, sellerCacheUpdateMap.getOrDefault(sellerId, 0) + cacheToUpdate);
        });

        // 모든 판매자에 대해 비관적 락으로 캐시 업데이트
        for (Map.Entry<Long, Integer> entry : sellerCacheUpdateMap.entrySet()) {
            User seller = userRepository.findByIdWithPessimisticLock(entry.getKey())
                    .orElseThrow(() -> new GeneralException(UserErrorStatus.USER_INFO_NOT_FOUND));
            seller.updateCache(entry.getValue());
        }

        order.updateStatus(Status.구매_확정);
    }
}

