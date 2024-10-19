package org.c4marathon.assignment.img.service;

import static org.assertj.core.api.Assertions.*;
import static org.c4marathon.assignment.img.dto.ImageFileExtension.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.net.URL;
import java.util.List;
import java.util.UUID;

import org.c4marathon.assignment.global.config.UUIDProvider;
import org.c4marathon.assignment.img.domain.repository.ImgRepository;
import org.c4marathon.assignment.img.dto.ImageUrlRequest;
import org.c4marathon.assignment.img.dto.ImageUrlResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

class S3ServiceTest {

	@Mock
	private AmazonS3Client amazonS3;

	@Mock
	private ImgRepository imgRepository;

	@Mock
	private UUIDProvider uuidProvider;

	@InjectMocks
	private S3Service s3Service;

	private final String bucket = "test-bucket";
	private final String prefix = "https://s3.amazonaws.com/test-bucket";

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		ReflectionTestUtils.setField(s3Service, "bucket", bucket);
		ReflectionTestUtils.setField(s3Service, "prefix", prefix);
	}

	@DisplayName("성공적으로 Presigned URL을 발급한다.")
	@Test
	void issuePresignedUrlSuccess() throws Exception {
		// Given
		String fileExtension = "jpg";
		String fixedUUID = "6702bb79-fab1-4378-98f4-7cba4a5d023b";  // 고정된 UUID 값
		String fileName = fixedUUID + "." + fileExtension;
		String presignedUrl = "https://s3.amazonaws.com/test-bucket/" + fileName;

		ImageUrlRequest request = new ImageUrlRequest(JPG);

		when(uuidProvider.randomUUID()).thenReturn(UUID.fromString(fixedUUID));

		// Presigned URL 생성에 대한 모킹
		when(amazonS3.generatePresignedUrl(any(GeneratePresignedUrlRequest.class)))
			.thenReturn(new URL(presignedUrl));

		// When
		ImageUrlResponse response = s3Service.issuePresignedUrl(request);

		// Then
		assertThat(response.presignedUrl()).isEqualTo(presignedUrl);
		assertThat(response.imgUrl()).isEqualTo(prefix + "/" + fileName);

		verify(imgRepository, times(1)).save(any());
	}

	@DisplayName("유효한 이미지 URL을 검증한다.")
	@Test
	void validateUrlSuccess() {
		// Given
		String validUrl = prefix + "/image1.jpg";

		// When
		boolean result = s3Service.validateUrl(validUrl);

		// Then
		assertThat(result).isTrue();
	}

	@DisplayName("유효하지 않은 이미지 URL을 검증한다.")
	@Test
	void validateUrlFailure() {
		// Given
		String invalidUrl = "https://invalid-url.com/image1.jpg";

		// When
		boolean result = s3Service.validateUrl(invalidUrl);

		// Then
		assertThat(result).isFalse();
	}

	@DisplayName("이미지를 성공적으로 삭제한다.")
	@Test
	void deleteImagesSuccess() {
		// Given
		String fileName = "image1.jpg";
		when(amazonS3.doesObjectExist(bucket, fileName)).thenReturn(true);

		// When
		s3Service.deleteImages(List.of(fileName));

		// Then
		verify(amazonS3, times(1)).deleteObject(argThat(request ->
			request.getBucketName().equals(bucket) && request.getKey().equals(fileName)
		));
	}

	@DisplayName("존재하지 않는 이미지를 삭제하지 않는다.")
	@Test
	void deleteImagesDoesNotExist() {
		// Given
		String fileName = "nonexistent.jpg";
		when(amazonS3.doesObjectExist(bucket, fileName)).thenReturn(false);

		// When
		s3Service.deleteImages(List.of(fileName));

		// Then
		verify(amazonS3, never()).deleteObject(any(DeleteObjectRequest.class));
	}

}
