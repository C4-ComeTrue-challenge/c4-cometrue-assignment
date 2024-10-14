package org.c4marathon.assignment.order.util;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class OrderKeyGenerator {

    public String generateOrderKey(Long memberId) {
        String id = UUID.randomUUID().toString().substring(0, 30);
        id += memberId;
        id += ".";
        id += LocalDateTime.now().getYear();
        id += LocalDateTime.now().getMonthValue();
        id += LocalDateTime.now().getDayOfMonth();
        return id;
    }
}
