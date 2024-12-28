package com.appspot.fherdelpino.foodie.controller;

import com.appspot.fherdelpino.foodie.model.Restaurant;
import com.appspot.fherdelpino.foodie.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurant")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping("/{id}")
    @ResponseBody
    public Restaurant getRestaurant(@PathVariable String id) {
        return restaurantService.getRestaurant(id).orElse(null);
    }

    @PostMapping
    @ResponseBody
    public Restaurant createRestaurant(@RequestBody Restaurant restaurant) {
        return restaurantService.createRestaurant(restaurant);
    }

}