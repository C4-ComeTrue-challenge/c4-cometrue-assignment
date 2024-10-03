package org.c4marathon.assignment.order.domain.repository;

import org.c4marathon.assignment.order.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders, String> {

    Optional<Orders> findByOrderId(String orderId);
}
