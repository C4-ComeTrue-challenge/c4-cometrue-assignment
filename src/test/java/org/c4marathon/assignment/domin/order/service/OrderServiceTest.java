package org.c4marathon.assignment.domin.order.service;

import org.c4marathon.assignment.domin.item.controller.ItemErrorStatus;
import org.c4marathon.assignment.domin.item.entity.Item;
import org.c4marathon.assignment.domin.item.repository.ItemRepository;
import org.c4marathon.assignment.domin.order.controller.OrderErrorStatus;
import org.c4marathon.assignment.domin.order.dto.OrderRequestDTO;
import org.c4marathon.assignment.domin.order.entity.Order;
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
                .build();

        OrderRequestDTO.OrderItemDTO orderItemDTO = new OrderRequestDTO.OrderItemDTO();
        orderItemDTO.setItemId(1L);
        orderItemDTO.setQuantity(2);

        createOrderDTO = new OrderRequestDTO.CreateOrderDTO();
        createOrderDTO.setOrderItems(Arrays.asList(orderItemDTO));
    }

    @Nested
    class CreateOrderTests {
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
}