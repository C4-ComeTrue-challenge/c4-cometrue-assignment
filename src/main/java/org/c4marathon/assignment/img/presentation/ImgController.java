package org.c4marathon.assignment.img.presentation;

import org.c4marathon.assignment.img.dto.ImageUrlRequest;
import org.c4marathon.assignment.img.dto.ImageUrlResponse;
import org.c4marathon.assignment.img.service.ImgService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/img")
@RequiredArgsConstructor
public class ImgController {

	private final ImgService imgService;

	@PostMapping("/presigned")
	public ResponseEntity<ImageUrlResponse> createPresigned(
		@RequestBody ImageUrlRequest request) {
		return ResponseEntity.ok(imgService.issuePresignedUrl(request));
	}
}
