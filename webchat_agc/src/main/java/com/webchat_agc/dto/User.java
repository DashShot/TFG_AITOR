package com.webchat_agc.dto;

import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@Document("User")
@NoArgsConstructor
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    @Field(name = "username")
    private String username;

    @JsonIgnore
    private String password;

    private List<String> roles;
    

    public User(String username, String password, String... roles) {
       this.id = generateRandomId();
        this.username = username;
        this.password = password;
        this.roles = List.of(roles);
    }

    public User(String username, String password, String roles) {
        this.id = generateRandomId();
         this.username = username;
         this.password = password;
         this.roles = List.of(roles);
     }

    private String generateRandomId() {
        return UUID.randomUUID().toString();
    }


}
