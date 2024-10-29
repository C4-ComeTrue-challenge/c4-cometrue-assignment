package org.c4marathon.assignment.img.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageFileExtension {
	JPEG("jpeg"),
	JPG("jpg"),
	PNG("png");

	private final String uploadExtension;
}
