package org.c4marathon.assignment.domin.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

public class ItemRequestDTO {

    @Data
    public static class ItemRegisterDTO {
        @NotBlank
        private String name;

        @NotNull
        private Integer price;

        @NotNull
        private Integer stock;

        private String description;
    }
}
