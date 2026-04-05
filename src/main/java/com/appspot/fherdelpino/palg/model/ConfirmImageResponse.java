package com.appspot.fherdelpino.palg.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ConfirmImageResponse {
    private String id;
    private String fileName;
    private String contentType;
    private Long size;
    private Instant createdAt;
}
