package org.c4marathon.assignment.order.domain.repository;

import org.c4marathon.assignment.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {

    Optional<Order> findByOrderId(String orderId);
}
