package com.appspot.fherdelpino.palg.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageResponse {
    private String id;
    private String url;
    private String fileName;
    private String contentType;
}
