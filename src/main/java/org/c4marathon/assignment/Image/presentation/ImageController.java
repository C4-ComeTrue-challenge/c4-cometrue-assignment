package org.c4marathon.assignment.Image.presentation;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.Image.presentation.dto.ImageDeleteRequest;
import org.c4marathon.assignment.Image.presentation.dto.ImageResponse;
import org.c4marathon.assignment.Image.service.AwsS3Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ImageController {

    private final AwsS3Service awsS3Service;

    @PostMapping("/image")
    public ResponseEntity<ImageResponse> uploadImage(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(awsS3Service.uploadImageToS3AndDb(multipartFile));
    }

    @DeleteMapping("/image")
    public ResponseEntity<Void> deleteImage(@RequestBody ImageDeleteRequest request) {
        awsS3Service.deleteImageFromS3AndDb(request.toServiceDto());

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
