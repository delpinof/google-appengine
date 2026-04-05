package com.appspot.fherdelpino.palg.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetImagesResponse {
    List<ImageResponse> images;
}
