package com.appspot.fherdelpino.foodie.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("restaurant")
@Data
@Builder
public class Restaurant {
    @Id
    private String id;
    private String name;
    private List<RestaurantBranch> branches;
    private List<String> websites;
}
