package com.webchat_agc.dto;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;

    private String nickName;
    private String password;

    private List<String> roles = null;
    private UserStatus status;

    public User(String nickName, String password, String rol) {
        this.nickName = nickName;
        this.password = password;
        this.roles.add(rol);
    }

}
