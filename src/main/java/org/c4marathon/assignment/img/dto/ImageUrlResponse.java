package org.c4marathon.assignment.img.dto;

public record ImageUrlResponse(
	String presignedUrl,
	String imgUrl) {
}
