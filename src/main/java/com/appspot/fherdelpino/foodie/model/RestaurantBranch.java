package com.appspot.fherdelpino.foodie.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("restaurant-branch")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantBranch {
    @Id
    private String id;
    private String branchName;
    private RestaurantAddress address;
    private String phoneNumber;
    private List<String> webSites;
}
