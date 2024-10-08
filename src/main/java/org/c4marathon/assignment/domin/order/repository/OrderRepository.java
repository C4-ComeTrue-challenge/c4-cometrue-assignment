package org.c4marathon.assignment.domin.order.repository;

import jakarta.persistence.LockModeType;
import org.c4marathon.assignment.domin.order.entity.Order;
import org.c4marathon.assignment.domin.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select o from Order o where o.id = :id")
    Optional<Order> findByIdWithPessimisticLock(@Param("id") Long id);
}
