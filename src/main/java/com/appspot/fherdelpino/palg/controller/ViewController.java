package com.appspot.fherdelpino.palg.controller;


import com.appspot.fherdelpino.palg.configuration.TenantContext;
import com.appspot.fherdelpino.palg.model.ImageModel;
import com.appspot.fherdelpino.palg.model.ImageResponse;
import com.appspot.fherdelpino.palg.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.time.Duration;
import java.util.List;

@Controller
public class ViewController {

    private static final String BUCKET_NAME = "palg-media-bucket";

    @Autowired
    private TenantContext tenantContext;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private S3Presigner s3Presigner;

    @GetMapping("/app")
    public String app(
            @AuthenticationPrincipal OidcUser user,
            @RegisteredOAuth2AuthorizedClient("cognito") OAuth2AuthorizedClient authorizedClient,
            Model model
    ) {
        String tenantId = tenantContext.getTenantId();
        List<ImageResponse> images = imageRepository.findByTenantId(tenantId).stream()
                .map(this::toResponse)
                .toList();

        model.addAttribute("images", images);
        model.addAttribute("userName", tenantContext.getUserName());
        model.addAttribute("tenantId", tenantId);
        model.addAttribute("accessToken", authorizedClient.getAccessToken().getTokenValue());

        return "app";
    }

    private ImageResponse toResponse(ImageModel image) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(image.getS3Key())
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .getObjectRequest(getObjectRequest)
                .build();

        return ImageResponse.builder()
                .id(image.getId())
                .url(s3Presigner.presignGetObject(presignRequest).url().toString())
                .fileName(image.getFileName())
                .contentType(image.getContentType())
                .build();
    }
}
