package com.webchat_agc.dto.messageModels;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class AuthMessage {
    
    private String username;
    private String  password;

    public AuthMessage(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
