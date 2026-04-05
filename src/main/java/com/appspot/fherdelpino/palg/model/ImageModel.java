package com.appspot.fherdelpino.palg.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "images")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ImageModel {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    private String s3Key;   // <-- where the file lives

    private String fileName;

    private String contentType;

    private Long size;

    private Instant createdAt;

}
