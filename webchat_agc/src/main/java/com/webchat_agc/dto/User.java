package com.webchat_agc.dto;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    private String username;

    @JsonIgnore
    private String password;

    private List<String> roles;
    

    public User(String username, String password, String... roles) {
        this.username = username;
        this.password = password;
        this.roles = List.of(roles);
    }

    public User(String username, String password, String roles) {
         this.username = username;
         this.password = password;
         this.roles = List.of(roles);
     }

}
