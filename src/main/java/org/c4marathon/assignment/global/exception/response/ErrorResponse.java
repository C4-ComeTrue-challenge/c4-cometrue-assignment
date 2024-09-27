package org.c4marathon.assignment.global.exception.response;


public record ErrorResponse(
        int code,
        String message) {
}
