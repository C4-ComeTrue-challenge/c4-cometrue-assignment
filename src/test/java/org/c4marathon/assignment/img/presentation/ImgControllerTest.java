package org.c4marathon.assignment.img.presentation;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.c4marathon.assignment.config.CommonControllerTest;
import org.c4marathon.assignment.global.config.SessionConfig;
import org.c4marathon.assignment.img.dto.ImageFileExtension;
import org.c4marathon.assignment.img.dto.ImageUrlRequest;
import org.c4marathon.assignment.img.dto.ImageUrlResponse;
import org.c4marathon.assignment.img.service.S3Service;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(ImgController.class)
class ImgControllerTest extends CommonControllerTest {

	@MockBean
	private S3Service s3Service;

	@MockBean
	private SessionConfig sessionConfig;

	@DisplayName("성공적으로 Presigned URL을 생성한다.")
	@Test
	void createPresignedUrlSuccess() throws Exception {
		// Given
		ImageUrlRequest request = new ImageUrlRequest(ImageFileExtension.JPG);
		ImageUrlResponse response = new ImageUrlResponse("presignedUrl",
			"https://s3.amazonaws.com/bucketname/image1.jpg");

		when(s3Service.issuePresignedUrl(any(ImageUrlRequest.class))).thenReturn(response);

		// When & Then
		mockMvc.perform(post("/api/img/presignedUrl").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.imgUrl").value(response.imgUrl()));
	}
}
