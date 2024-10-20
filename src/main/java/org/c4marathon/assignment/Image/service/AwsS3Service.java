package org.c4marathon.assignment.Image.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.Image.domain.Image;
import org.c4marathon.assignment.Image.domain.repository.ImageRepository;
import org.c4marathon.assignment.Image.exception.ImageDeleteException;
import org.c4marathon.assignment.Image.exception.ImageSaveFailedException;
import org.c4marathon.assignment.Image.exception.NotFoundImageException;
import org.c4marathon.assignment.Image.presentation.dto.ImageResponse;
import org.c4marathon.assignment.Image.service.dto.ImageDeleteServiceRequest;
import org.c4marathon.assignment.global.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AwsS3Service {
    private final AmazonS3Client amazonS3Client;
    private final ImageRepository imageRepository;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;


    /*
    * Image S3에 저장 후 DB 에 메타데이터 저장
    * DB에 저장 실패 시 S3에 저장했던 이미지 삭제
    * */
    public ImageResponse uploadImageToS3AndDb(MultipartFile multipartFile) throws IOException {
        String s3FileName = getS3FileName(multipartFile);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3Client.putObject(bucketName, s3FileName, multipartFile.getInputStream(), metadata);
        String imageUrl = amazonS3Client.getUrl(bucketName, s3FileName).toString();

        try {
            createImage(s3FileName, imageUrl);
        } catch (Exception e) {
            amazonS3Client.deleteObject(bucketName, s3FileName);
            throw new ImageSaveFailedException(ErrorCode.DB_SAVE_FAILED);
        }
        
        return new ImageResponse(imageUrl);
    }

    public void deleteImageFromS3AndDb(ImageDeleteServiceRequest request) {
        Image image = imageRepository.findByImageUrl(request.imageUrl())
                .orElseThrow(() -> new NotFoundImageException(ErrorCode.NOT_FOUND_IMAGE));

        try {
            deleteImageFromS3(image.getImageUrl());
        } catch (Exception e) {
            throw new ImageDeleteException(ErrorCode.S3_DELETE_FAILED);
        }
        deleteImageFromDb(image);
    }

    private void createImage(String s3FileName, String imageUrl) {
        Image image = Image.of(s3FileName, imageUrl);
        imageRepository.save(image);
    }

    private void deleteImageFromS3(String imageUrl) {
        String fileKey = extractFileKeyFromUrl(imageUrl);
        amazonS3Client.deleteObject(bucketName, fileKey);
    }

    private void deleteImageFromDb(Image image) {
        imageRepository.delete(image);
    }

    private String getS3FileName(MultipartFile multipartFile) {
        return UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();
    }


    private String extractFileKeyFromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            return url.getPath().substring(1);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid S3 URL: " + imageUrl, e);
        }
    }

}
