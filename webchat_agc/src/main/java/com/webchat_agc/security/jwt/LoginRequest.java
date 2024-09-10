package com.webchat_agc.security.jwt;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginRequest {
    
    private String username;
	private String password;

	public LoginRequest() {
	}

	public LoginRequest(String username, String password) {
		this.username = username;
		this.password = password;
	}
}
