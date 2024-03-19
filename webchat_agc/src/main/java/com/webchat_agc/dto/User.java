package com.webchat_agc.dto;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document("User")
public class User {

    //@Id
    //private String id;
    @Id
    private String username;
    private String password;
    
    

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    // private List<String> roles = null;

    // public User(String username, String password, String rol) {
    //     this.username = username;
    //     this.password = password;
    //     this.roles.add(rol);
    // }

}
