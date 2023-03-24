package com.appspot.fherdelpino.security.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("users")
@Data
public class User {
    @Id
    private String userName;
    private String password;
    private List<Role> roles;

}
