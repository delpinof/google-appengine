package com.appspot.fherdelpino.security.repository;

import com.appspot.fherdelpino.security.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

}
