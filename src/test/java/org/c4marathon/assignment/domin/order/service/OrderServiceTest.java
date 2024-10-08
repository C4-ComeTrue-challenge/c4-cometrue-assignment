package org.c4marathon.assignment.domin.order.service;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderService orderService;

    private User user;
    private Item item;
    private Order order;
    private OrderItem orderItem;
    private OrderRequestDTO.CreateOrderDTO createOrderDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = User.builder()
                .id(1L)
                .cache(10000)
                .build();

        item = Item.builder()
                .id(1L)
                .price(1000)
                .stock(10)
                .user(user)
                .build();

        orderItem = OrderItem.builder()
                .item(item)
                .orderPrice(1000)
                .orderQuantity(2)
                .build();

        order = Order.builder()
                .id(1L)
                .user(user)
                .orderItems(Arrays.asList(orderItem))
                .orderStatus(Status.주문_완료)
                .totalOrderPrice(2000)
                .build();

        OrderRequestDTO.OrderItemDTO orderItemDTO = new OrderRequestDTO.OrderItemDTO();
        orderItemDTO.setItemId(1L);
        orderItemDTO.setQuantity(2);

        createOrderDTO = new OrderRequestDTO.CreateOrderDTO();
        createOrderDTO.setOrderItems(Arrays.asList(orderItemDTO));
    }

    @Nested
    class 상품_주문_테스트 {
        @Test
        void 주문_성공() {
            when(userRepository.findByIdWithPessimisticLock(1L)).thenReturn(Optional.of(user));
            when(itemRepository.findByIdWithPessimisticLock(1L)).thenReturn(Optional.of(item));

            assertDoesNotThrow(() -> orderService.createOrder(createOrderDTO, user.getId()));

            verify(userRepository).findByIdWithPessimisticLock(1L);
            verify(itemRepository).findByIdWithPessimisticLock(1L);
            verify(orderRepository).save(any(Order.class));
            assertEquals(8, item.getStock());
            assertEquals(8000, user.getCache());
        }

        @Test
        void 주문_실패_유저_없음() {
            when(userRepository.findByIdWithPessimisticLock(1L)).thenReturn(Optional.empty());

            GeneralException exception = assertThrows(GeneralException.class,
                    () -> orderService.createOrder(createOrderDTO, user.getId()));

            assertEquals(UserErrorStatus.USER_INFO_NOT_FOUND, exception.getCode());
            verify(userRepository).findByIdWithPessimisticLock(1L);
            verify(itemRepository, never()).findByIdWithPessimisticLock(anyLong());
            verify(orderRepository, never()).save(any(Order.class));
        }

        @Test
        void 주문_실패_상품_없음() {
            when(userRepository.findByIdWithPessimisticLock(1L)).thenReturn(Optional.of(user));
            when(itemRepository.findByIdWithPessimisticLock(1L)).thenReturn(Optional.empty());

            GeneralException exception = assertThrows(GeneralException.class,
                    () -> orderService.createOrder(createOrderDTO, user.getId()));

            assertEquals(ItemErrorStatus.ITEM_INFO_NOT_FOUND, exception.getCode());
            verify(userRepository).findByIdWithPessimisticLock(1L);
            verify(itemRepository).findByIdWithPessimisticLock(1L);
            verify(orderRepository, never()).save(any(Order.class));
        }

        @Test
        void 주문_실패_상품_부족() {
            item.decreaseStock(9);
            when(userRepository.findByIdWithPessimisticLock(1L)).thenReturn(Optional.of(user));
            when(itemRepository.findByIdWithPessimisticLock(1L)).thenReturn(Optional.of(item));

            GeneralException exception = assertThrows(GeneralException.class,
                    () -> orderService.createOrder(createOrderDTO, user.getId()));

            assertEquals(OrderErrorStatus.INVALID_CREAT_ORDER, exception.getCode());
            verify(userRepository).findByIdWithPessimisticLock(1L);
            verify(itemRepository).findByIdWithPessimisticLock(1L);
            verify(orderRepository, never()).save(any(Order.class));
            assertEquals(1, item.getStock());
        }

        @Test
        void 주문_실패_유저_캐시_부족() {
            user.decreaseCache(9000);
            when(userRepository.findByIdWithPessimisticLock(1L)).thenReturn(Optional.of(user));
            when(itemRepository.findByIdWithPessimisticLock(1L)).thenReturn(Optional.of(item));

            GeneralException exception = assertThrows(GeneralException.class,
                    () -> orderService.createOrder(createOrderDTO, user.getId()));

            assertEquals(OrderErrorStatus.REJECT_PAYMENT, exception.getCode());
            verify(userRepository).findByIdWithPessimisticLock(1L);
            verify(itemRepository).findByIdWithPessimisticLock(1L);
            verify(orderRepository, never()).save(any(Order.class));
            assertAll(
                    () -> assertEquals(8, item.getStock()),
                    () -> assertEquals(1000, user.getCache())
            );
        }

        @Test
        void 주문_성공_여러개_주문() {
            Item item2 = Item.builder().id(2L).price(500).stock(5).build();
            OrderRequestDTO.OrderItemDTO orderItemDTO2 = new OrderRequestDTO.OrderItemDTO();
            orderItemDTO2.setItemId(2L);
            orderItemDTO2.setQuantity(1);

            // 새로운 변경 가능한 리스트 생성
            List<OrderRequestDTO.OrderItemDTO> newOrderItems = new ArrayList<>(createOrderDTO.getOrderItems());
            newOrderItems.add(orderItemDTO2);
            createOrderDTO.setOrderItems(newOrderItems);

            when(userRepository.findByIdWithPessimisticLock(1L)).thenReturn(Optional.of(user));
            when(itemRepository.findByIdWithPessimisticLock(1L)).thenReturn(Optional.of(item));
            when(itemRepository.findByIdWithPessimisticLock(2L)).thenReturn(Optional.of(item2));

            assertDoesNotThrow(() -> orderService.createOrder(createOrderDTO, user.getId()));

            verify(userRepository).findByIdWithPessimisticLock(1L);
            verify(itemRepository).findByIdWithPessimisticLock(1L);
            verify(itemRepository).findByIdWithPessimisticLock(2L);
            verify(orderRepository).save(any(Order.class));
            assertAll(
                    () -> assertEquals(8, item.getStock()),
                    () -> assertEquals(4, item2.getStock()),
                    () -> assertEquals(7500, user.getCache())
            );
        }
    }

    @Nested
    class 상품_환불_테스트 {
        @Test
        void 환불_성공() {
            when(userRepository.findByIdWithPessimisticLock(1L)).thenReturn(Optional.of(user));
            when(orderRepository.findByIdWithPessimisticLock(1L)).thenReturn(Optional.of(order));
            when(itemRepository.findByIdWithPessimisticLock(1L)).thenReturn(Optional.of(item));

            assertDoesNotThrow(() -> orderService.refundOrder(1L, 1L));

            assertEquals(Status.취소, order.getOrderStatus());
            assertEquals(12000, user.getCache());
            assertEquals(12, item.getStock());
            verify(userRepository).findByIdWithPessimisticLock(1L);
            verify(orderRepository).findByIdWithPessimisticLock(1L);
            verify(itemRepository).findByIdWithPessimisticLock(1L);
        }

        @Test
        void 환불_실패_사용자_없음() {
            when(userRepository.findByIdWithPessimisticLock(1L)).thenReturn(Optional.empty());

            assertThrows(GeneralException.class, () -> orderService.refundOrder(1L, 1L));
            verify(userRepository).findByIdWithPessimisticLock(1L);
            verify(orderRepository, never()).findByIdWithPessimisticLock(anyLong());
        }

        @Test
        void 환불_실패_주문_없음() {
            when(userRepository.findByIdWithPessimisticLock(1L)).thenReturn(Optional.of(user));
            when(orderRepository.findByIdWithPessimisticLock(1L)).thenReturn(Optional.empty());

            assertThrows(GeneralException.class, () -> orderService.refundOrder(1L, 1L));
            verify(userRepository).findByIdWithPessimisticLock(1L);
            verify(orderRepository).findByIdWithPessimisticLock(1L);
            verify(itemRepository, never()).findByIdWithPessimisticLock(anyLong());
        }

        @Test
        void 환불_실패_잘못된_주문_상태() {
            order.updateStatus(Status.취소);
            when(userRepository.findByIdWithPessimisticLock(1L)).thenReturn(Optional.of(user));
            when(orderRepository.findByIdWithPessimisticLock(1L)).thenReturn(Optional.of(order));

            assertThrows(GeneralException.class, () -> orderService.refundOrder(1L, 1L));
            verify(userRepository).findByIdWithPessimisticLock(1L);
            verify(orderRepository).findByIdWithPessimisticLock(1L);
            verify(itemRepository, never()).findByIdWithPessimisticLock(anyLong());
        }

        @Test
        void 환불_실패_권한_없음() {
            User otherUser = User.builder().id(2L).build();
            when(userRepository.findByIdWithPessimisticLock(2L)).thenReturn(Optional.of(otherUser));
            when(orderRepository.findByIdWithPessimisticLock(1L)).thenReturn(Optional.of(order));

            assertThrows(GeneralException.class, () -> orderService.refundOrder(1L, 2L));
            verify(userRepository).findByIdWithPessimisticLock(2L);
            verify(orderRepository).findByIdWithPessimisticLock(1L);
            verify(itemRepository, never()).findByIdWithPessimisticLock(anyLong());
        }

        @Test
        void 환불_실패_상품_없음() {
            when(userRepository.findByIdWithPessimisticLock(1L)).thenReturn(Optional.of(user));
            when(orderRepository.findByIdWithPessimisticLock(1L)).thenReturn(Optional.of(order));
            when(itemRepository.findByIdWithPessimisticLock(1L)).thenReturn(Optional.empty());

            assertThrows(GeneralException.class, () -> orderService.refundOrder(1L, 1L));
            verify(userRepository).findByIdWithPessimisticLock(1L);
            verify(orderRepository).findByIdWithPessimisticLock(1L);
            verify(itemRepository).findByIdWithPessimisticLock(1L);
        }
    }

    @Nested
    class 구매_확정_테스트 {
        @Test
        void 성공_판매자캐시업데이트() {
            // given
            when(userRepository.findByIdWithPessimisticLock(1L)).thenReturn(Optional.of(user));
            when(orderRepository.findByIdWithPessimisticLock(1L)).thenReturn(Optional.of(order));

            // when
            assertDoesNotThrow(() -> orderService.confirmOrder(1L));

            // then
            verify(userRepository).findByIdWithPessimisticLock(1L);
            verify(orderRepository).findByIdWithPessimisticLock(1L);
            assertAll(
                    () -> assertEquals(11900, user.getCache()), // 10000 + 2000 * 0.95
                    () -> assertEquals(Status.구매_확정, order.getOrderStatus())
            );

        }

        @Test
        void 실패_주문_없음() {
            // given
            when(userRepository.findByIdWithPessimisticLock(1L)).thenReturn(Optional.of(user));
            when(orderRepository.findByIdWithPessimisticLock(1L)).thenReturn(Optional.empty());

            // when
            GeneralException exception = assertThrows(GeneralException.class, () -> orderService.confirmOrder(1L));

            // then
            assertEquals(OrderErrorStatus.NOT_FOUND_ORDER, exception.getCode());
        }

        @Test
        void 실패_판매자_없음() {
            // given
            when(userRepository.findByIdWithPessimisticLock(1L)).thenReturn(Optional.empty());
            when(orderRepository.findByIdWithPessimisticLock(1L)).thenReturn(Optional.of(order));

            // when
            GeneralException exception = assertThrows(GeneralException.class, () -> orderService.confirmOrder(1L));

            // then
            assertEquals(UserErrorStatus.USER_INFO_NOT_FOUND, exception.getCode());
        }
    }
}