package com.appspot.fherdelpino.foodie.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("restaurant-address")
@Data
@Builder
public class RestaurantAddress {
    @Id
    private String id;
    private String street;
    private String streetNumber;
    private String city;
    private String state;
    private String zipCode;
}
