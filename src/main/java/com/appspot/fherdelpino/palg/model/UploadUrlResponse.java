package com.appspot.fherdelpino.palg.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UploadUrlResponse {
    private String uploadUrl;
    private String key;
}
