package org.c4marathon.assignment.img.presentation;

import org.c4marathon.assignment.img.dto.ImageUrlRequest;
import org.c4marathon.assignment.img.dto.ImageUrlResponse;
import org.c4marathon.assignment.img.service.S3Service;
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

	private final S3Service s3Service;

	@PostMapping("/presigned")
	public ResponseEntity<ImageUrlResponse> createPresigned(
		@RequestBody ImageUrlRequest request) {
		return ResponseEntity.ok(s3Service.issuePresignedUrl(request));
	}
}
