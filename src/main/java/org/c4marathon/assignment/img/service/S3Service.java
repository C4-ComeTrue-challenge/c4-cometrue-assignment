package org.c4marathon.assignment.img.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import org.c4marathon.assignment.img.domain.Img;
import org.c4marathon.assignment.img.domain.repository.ImgRepository;
import org.c4marathon.assignment.img.dto.ImageUrlRequest;
import org.c4marathon.assignment.img.dto.ImageUrlResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class S3Service {

	private final AmazonS3Client amazonS3;
	private final ImgRepository imgRepository;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	@Value("${image.prefix}")
	private String prefix;

	public ImageUrlResponse issuePresignedUrl(ImageUrlRequest request) {

		String valueFileExtension = request.imageFileExtension().getUploadExtension();

		String fileName = createFileName(valueFileExtension);

		String imgUrl = prefix + "/" + fileName;

		GeneratePresignedUrlRequest presignedUrlRequest = getGeneratePreSignedUrlRequest(bucket, fileName);
		String url = amazonS3.generatePresignedUrl(presignedUrlRequest).toString();

		imgRepository.save(Img.builder().fileName(fileName).build());

		return new ImageUrlResponse(url, imgUrl);
	}

	public boolean validateUrl(String url) {
		return url != null && url.startsWith(prefix);
	}

	public void deleteImages(List<String> fileNames) {
		for (String fileName : fileNames) {
			if (amazonS3.doesObjectExist(bucket, fileName)) {
				amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
			}
		}
	}

	private String createFileName(String fileExtension) {
		return UUID.randomUUID() + "." + fileExtension;
	}

	private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(String bucket, String fileName) {
		GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket,
			fileName).withMethod(HttpMethod.PUT).withExpiration(getPresignedUrlExpiration());
		generatePresignedUrlRequest.addRequestParameter(Headers.S3_CANNED_ACL,
			CannedAccessControlList.PublicRead.toString());
		return generatePresignedUrlRequest;
	}

	private Date getPresignedUrlExpiration() {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
		calendar.add(Calendar.MINUTE, 5);
		return calendar.getTime();
	}
}
