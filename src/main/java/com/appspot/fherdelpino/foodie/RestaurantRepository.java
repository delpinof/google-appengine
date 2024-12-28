package com.appspot.fherdelpino.foodie;

import com.appspot.fherdelpino.foodie.model.Restaurant;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RestaurantRepository extends MongoRepository<Restaurant, String> {
}
