package org.c4marathon.assignment.order.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class OrderKeyGenerator {

    public String generateOrderKey(Long memberId) {
        String id = RandomStringUtils.randomAlphanumeric(30);
        id += memberId;
        id += ".";
        id += LocalDateTime.now().getYear();
        id += LocalDateTime.now().getMonthValue();
        id += LocalDateTime.now().getDayOfMonth();
        return id;
    }
}
