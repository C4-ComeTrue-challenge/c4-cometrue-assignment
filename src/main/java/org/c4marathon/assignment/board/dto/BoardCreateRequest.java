package org.c4marathon.assignment.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BoardCreateRequest(
        @NotBlank
        String content,

        @NotBlank
        @Size(max=100)
        String title,
        String writerName,
        String password
) {
}
