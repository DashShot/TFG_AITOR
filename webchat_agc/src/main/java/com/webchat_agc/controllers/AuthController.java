package com.webchat_agc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.webchat_agc.security.jwt.AuthResponse;
import com.webchat_agc.security.jwt.AuthResponse.Status;
import com.webchat_agc.security.jwt.LoginRequest;
import com.webchat_agc.security.jwt.UserLoginService;
import com.webchat_agc.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Controller
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private UserService userService;
    

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
        @CookieValue(name = "accessToken", required = false) String accessToken,
		@CookieValue(name = "refreshToken", required = false) String refreshToken,
		@RequestBody LoginRequest loginRequest) {
		System.out.println("PRUEBA-------JWT------------");

        System.out.println(loginRequest.getUsername());

        return userLoginService.login(loginRequest, accessToken, refreshToken);
    }
    
    @PostMapping("/refresh")
	public ResponseEntity<AuthResponse> refreshToken(
			@CookieValue(name = "refreshToken", required = false) String refreshToken) {

		return userLoginService.refresh(refreshToken);
	}

	@PostMapping("/logout")
	public ResponseEntity<AuthResponse> logOut(HttpServletRequest request, HttpServletResponse response) {

		return ResponseEntity.ok(new AuthResponse(Status.SUCCESS, userLoginService.logout(request, response)));
	}

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
        @CookieValue(name = "accessToken", required = false) String accessToken,
		@CookieValue(name = "refreshToken", required = false) String refreshToken,
		@RequestBody LoginRequest loginRequest) {
    

        return userLoginService.register(loginRequest, accessToken, refreshToken);
    }

    // @GetMapping("/xsrf-token")
    // public ResponseEntity<String> getXSRFToken(HttpServletRequest request) {
    //     String xsrfToken = request.getSession().getAttribute("_csrf").toString();
    //     return ResponseEntity.ok(xsrfToken);
    // }
    

}
