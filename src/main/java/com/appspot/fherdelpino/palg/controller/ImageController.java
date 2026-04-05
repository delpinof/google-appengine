package com.appspot.fherdelpino.palg.controller;

import com.appspot.fherdelpino.error.ResourceNotFoundException;
import com.appspot.fherdelpino.error.UnauthorizedException;
import com.appspot.fherdelpino.palg.configuration.TenantContext;
import com.appspot.fherdelpino.palg.model.ConfirmImageRequest;
import com.appspot.fherdelpino.palg.model.ConfirmImageResponse;
import com.appspot.fherdelpino.palg.model.GetImagesResponse;
import com.appspot.fherdelpino.palg.model.ImageModel;
import com.appspot.fherdelpino.palg.model.ImageResponse;
import com.appspot.fherdelpino.palg.model.UploadUrlRequest;
import com.appspot.fherdelpino.palg.model.UploadUrlResponse;
import com.appspot.fherdelpino.palg.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/images")
public class ImageController {

    private static final String BUCKET_NAME = "palg-media-bucket";

    @Autowired
    private TenantContext tenantContext;

    @Autowired
    private S3Presigner s3Presigner;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private S3Client s3Client;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String imageId) {
        String tenantId = tenantContext.getTenantId();
        ImageModel image = imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found"));

        if (!image.getTenantId().equals(tenantId)) {
            throw new UnauthorizedException("Unauthorized");
        }

        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(image.getS3Key())
                .build();

        DeleteObjectResponse response = s3Client.deleteObject(deleteRequest);

        imageRepository.delete(image);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public GetImagesResponse getImages() {
        String tenantId = tenantContext.getTenantId();

        List<ImageModel> imageModels = imageRepository.findByTenantId(tenantId);

        List<ImageResponse> imageResponses = imageModels.stream()
                .map(this::toResponse)
                .toList();

        return GetImagesResponse.builder()
                .images(imageResponses)
                .build();
    }

    @PostMapping("/confirm")
    public ConfirmImageResponse confirm(@RequestBody ConfirmImageRequest request) {

        String key = request.getKey();
        String tenantId = tenantContext.getTenantId();

        if (!request.getKey().startsWith(tenantId)) {
            throw new UnauthorizedException("Wrong Key");
        }

        HeadObjectResponse head;

        try {
            head = s3Client.headObject(HeadObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(key)
                    .build());
        } catch (NoSuchKeyException e) {
            throw new ResourceNotFoundException("File not uploaded");
        }

        ImageModel imageModel = ImageModel.builder()
                .id(UUID.randomUUID().toString())
                .s3Key(key)
                .size(head.contentLength())
                .contentType(head.contentType())
                .fileName(key.substring(key.lastIndexOf("/")))
                .tenantId(tenantId)
                .createdAt(head.lastModified())
                .build();
        imageModel = imageRepository.save(imageModel);


        return ConfirmImageResponse.builder()
                .id(imageModel.getId())
                .size(imageModel.getSize())
                .fileName(imageModel.getFileName())
                .createdAt(imageModel.getCreatedAt())
                .contentType(imageModel.getContentType())
                .build();
    }

    @PostMapping("/upload-url")
    public UploadUrlResponse uploadUrl(@RequestBody UploadUrlRequest request) {
        String extension = extractAndValidateExtension(request.getFileName());
        String tenantId = tenantContext.getTenantId();

        String key = tenantId + "/" + UUID.randomUUID() + extension;
        String presignedUrl = generatePresignedUrl(key, request.getContentType());
        return UploadUrlResponse.builder()
                .key(key)
                .uploadUrl(presignedUrl)
                .build();
    }

    private String extractAndValidateExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            throw new IllegalArgumentException("Invalid file");
        }

        String ext = filename.substring(filename.lastIndexOf(".")).toLowerCase();

        Set<String> allowed = Set.of(".jpg", ".png", ".webp");

        if (!allowed.contains(ext)) {
            throw new IllegalArgumentException("Invalid file type");
        }

        return ext;
    }

    private String generatePresignedUrl(String key, String contentType) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .contentType(contentType)
                .key(key)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5))
                .putObjectRequest(putObjectRequest)
                .build();

        PresignedPutObjectRequest presignedRequest =
                s3Presigner.presignPutObject(presignRequest);

        return presignedRequest.url().toString();
    }

    public ImageResponse toResponse(ImageModel image) {

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(image.getS3Key())
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .getObjectRequest(getObjectRequest)
                .build();

        String url = s3Presigner.presignGetObject(presignRequest)
                .url()
                .toString();

        return ImageResponse.builder()
                .id(image.getId())
                .url(url)
                .fileName(image.getFileName())
                .contentType(image.getContentType())
                .build();
    }
}
