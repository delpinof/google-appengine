package com.appspot.fherdelpino.foodie.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
class RestaurantSerDeTest {
    private static String restaurantPayload;

    private static Restaurant restaurantObject;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    public static void setUp() throws IOException {
        byte[] allBytes = RestaurantSerDeTest.class.getClassLoader().getResourceAsStream("payloads/restaurant/restaurant_payload1.json").readAllBytes();
        restaurantPayload = new String(allBytes);
        restaurantObject = Restaurant.builder().id("1")
                .name("Restaurant 1")
                .branches(List.of(
                        RestaurantBranch.builder().id("1")
                                .branchName("Branch 1")
                                .address(RestaurantAddress.builder()
                                        .id("1")
                                        .street("Street")
                                        .streetNumber("1")
                                        .city("City 1")
                                        .state("State 1")
                                        .zipCode("12345")
                                        .build())
                                .phoneNumber("1234567890")
                                .webSites(List.of("www.branch1.com", "www.branch1.org"))
                                .build(),
                        RestaurantBranch.builder().id("2")
                                .branchName("Branch 2")
                                .address(RestaurantAddress.builder()
                                        .id("2")
                                        .city("City 2")
                                        .state("State 2")
                                        .street("Street")
                                        .streetNumber("2")
                                        .zipCode("54321")
                                        .build())
                                .phoneNumber("1234567890")
                                .webSites(List.of("www.branch2.com", "www.branch2.org"))
                                .build()
                ))
                .webSites(List.of("www.restaurant1.com", "www.restaurant1.org"))
                .build();
    }

    @Test
    public void restaurantSerializationTest() throws IOException {
        JsonNode expectedRestaurant = objectMapper.readTree(restaurantPayload);
        JsonNode actualRestaurant = objectMapper.valueToTree(restaurantObject);
        assertThat(actualRestaurant).isEqualTo(expectedRestaurant);
    }

    @Test
    public void restaurantDeserializationTest() throws IOException {
        Restaurant actualRestaurant = objectMapper.readValue(restaurantPayload, Restaurant.class);
        assertThat(actualRestaurant).isEqualTo(restaurantObject);
    }

}