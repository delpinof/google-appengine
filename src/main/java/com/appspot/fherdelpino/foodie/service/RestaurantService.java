package com.appspot.fherdelpino.foodie.service;

import com.appspot.fherdelpino.foodie.RestaurantRepository;
import com.appspot.fherdelpino.foodie.model.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    public Restaurant createRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    public Optional<Restaurant> getRestaurant(String id) {
        return restaurantRepository.findById(id);
    }
}
