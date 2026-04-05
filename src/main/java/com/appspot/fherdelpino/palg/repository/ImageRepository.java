package com.appspot.fherdelpino.palg.repository;

import com.appspot.fherdelpino.palg.model.ImageModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends MongoRepository<ImageModel, String> {

    List<ImageModel> findByTenantId(String tenantId);

}
