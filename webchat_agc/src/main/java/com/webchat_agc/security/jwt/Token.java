package com.webchat_agc.security.jwt;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Token {

    private TokenType tokenType;
	private String tokenValue;
	private Long duration;
	private LocalDateTime expiryDate;

	public enum TokenType {
		ACCESS, REFRESH
	}

	public Token(TokenType tokenType, String tokenValue, Long duration, LocalDateTime expiryDate) {
		super();
		this.tokenType = tokenType;
		this.tokenValue = tokenValue;
		this.duration = duration;
		this.expiryDate = expiryDate;
	}
}
