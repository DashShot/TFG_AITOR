package com.webchat_agc.security.jwt;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AuthResponse {
    
    private Status status;
    private String message;
    private String error;
    

    public enum Status {
        SUCCESS,
        FAILURE
    }
    
    public AuthResponse(){

    }
    
    public AuthResponse(Status status, String message) {
		this.status = status;
		this.message = message;
	}

    public AuthResponse(Status status, String message, String error){
        this.status  = status;
        this.message  = message;
        this.error = error;
    }

}
