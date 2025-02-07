package com.webchat_agc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.webchat_agc.dto.User;
import com.webchat_agc.security.jwt.AuthResponse;
import com.webchat_agc.security.jwt.JwtCookieManager;
import com.webchat_agc.security.jwt.JwtTokenProvider;
import com.webchat_agc.security.jwt.LoginRequest;
import com.webchat_agc.security.jwt.SecurityCipher;
import com.webchat_agc.security.jwt.Token;
import com.webchat_agc.security.jwt.AuthResponse.Status;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Service
public class UserLoginService {
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

	@Autowired
	private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private JwtCookieManager cookieUtil;

    public ResponseEntity<AuthResponse> login(LoginRequest loginRequest, String encryptedAccessToken, String encryptedRefreshToken) {
    
		System.out.println("BUSCANDO USUARIO.....");
		
		try {
			// Try to authenticate the user using the provided username and password
			Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
			);
	
			// Proceed with the rest of the authentication process
			SecurityContextHolder.getContext().setAuthentication(authentication);
	
			String accessToken = SecurityCipher.decrypt(encryptedAccessToken);
			String refreshToken = SecurityCipher.decrypt(encryptedRefreshToken);
	
			String username = loginRequest.getUsername();
			UserDetails user = userDetailsService.loadUserByUsername(username);
	
			Boolean accessTokenValid = jwtTokenProvider.validateToken(accessToken);
			Boolean refreshTokenValid = jwtTokenProvider.validateToken(refreshToken);
	
			HttpHeaders responseHeaders = new HttpHeaders();
			Token newAccessToken;
			Token newRefreshToken;
	
			// Handle tokens based on validity
			if (!accessTokenValid && !refreshTokenValid) {
				newAccessToken = jwtTokenProvider.generateToken(user);
				newRefreshToken = jwtTokenProvider.generateRefreshToken(user);
				addAccessTokenCookie(responseHeaders, newAccessToken);
				addRefreshTokenCookie(responseHeaders, newRefreshToken);
			}
	
			if (!accessTokenValid && refreshTokenValid) {
				newAccessToken = jwtTokenProvider.generateToken(user);
				addAccessTokenCookie(responseHeaders, newAccessToken);
			}
	
			if (accessTokenValid && refreshTokenValid) {
				newAccessToken = jwtTokenProvider.generateToken(user);
				newRefreshToken = jwtTokenProvider.generateRefreshToken(user);
				addAccessTokenCookie(responseHeaders, newAccessToken);
				addRefreshTokenCookie(responseHeaders, newRefreshToken);
			}
	
			// If authentication is successful, return success response
			AuthResponse loginResponse = new AuthResponse(AuthResponse.Status.SUCCESS, "Auth successful. Tokens are created in cookie.");
			return ResponseEntity.ok().headers(responseHeaders).body(loginResponse);
	
		} catch (BadCredentialsException ex) {
			// Handle case where username or password is incorrect
			AuthResponse errorResponse = new AuthResponse(AuthResponse.Status.FAILURE, "Usuario o contraseña incorrectos.");
			return ResponseEntity.ok().body(errorResponse);
		}
	}

    public ResponseEntity<AuthResponse> refresh(String encryptedRefreshToken) {
		
		String refreshToken = SecurityCipher.decrypt(encryptedRefreshToken);
		
		Boolean refreshTokenValid = jwtTokenProvider.validateToken(refreshToken);
		
		if (!refreshTokenValid) {
			AuthResponse loginResponse = new AuthResponse(AuthResponse.Status.FAILURE,
					"Invalid refresh token !");
			return ResponseEntity.ok().body(loginResponse);
		}

		String username = jwtTokenProvider.getUsername(refreshToken);
		UserDetails user = userDetailsService.loadUserByUsername(username);
				
		Token newAccessToken = jwtTokenProvider.generateToken(user);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add(HttpHeaders.SET_COOKIE, cookieUtil
				.createAccessTokenCookie(newAccessToken.getTokenValue(), newAccessToken.getDuration()).toString());

		AuthResponse loginResponse = new AuthResponse(AuthResponse.Status.SUCCESS,
				"Auth successful. Tokens are created in cookie.");
		return ResponseEntity.ok().headers(responseHeaders).body(loginResponse);
	}

    public String getUserName() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication.getName();
	}

    public String logout(HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession(false);
		SecurityContextHolder.clearContext();
		session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}

		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				cookie.setMaxAge(0);
				cookie.setValue("");
				cookie.setHttpOnly(false);
				cookie.setPath("/");
				response.addCookie(cookie);
			}
		}

		return "logout successfully";
	}

    private void addAccessTokenCookie(HttpHeaders httpHeaders, Token token){
        httpHeaders.add(HttpHeaders.SET_COOKIE, cookieUtil.createAccessTokenCookie(token.getTokenValue(), token.getDuration()).toString());
    }

    private void addRefreshTokenCookie(HttpHeaders httpHeaders, Token token){
        httpHeaders.add(HttpHeaders.SET_COOKIE, cookieUtil.createRefreshTokenCookie(token.getTokenValue(), token.getDuration()).toString());
    }

	public ResponseEntity<AuthResponse> register(LoginRequest loginRequest, String encryptedAccessToken, String encryptedRefreshToken){

		if(!userService.getByUsername(loginRequest.getUsername()).isPresent()){
            
            //CAMBIAR PARA ENVIAR POR HTTP LA CONTRASEÑA ENCRIPTADA
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            User newUser = new  User(loginRequest.getUsername(), passwordEncoder.encode(loginRequest.getPassword()), "USER");

            userService.saveUser(newUser);

			Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));
 


			SecurityContextHolder.getContext().setAuthentication(authentication);

			String accessToken = SecurityCipher.decrypt(encryptedAccessToken);
			String refreshToken = SecurityCipher.decrypt(encryptedRefreshToken);
			

			
			String username = loginRequest.getUsername();
			UserDetails user = userDetailsService.loadUserByUsername(username);

			Boolean accessTokenValid = jwtTokenProvider.validateToken(accessToken);
			Boolean refreshTokenValid = jwtTokenProvider.validateToken(refreshToken);

			HttpHeaders responseHeaders = new HttpHeaders();
			Token newAccessToken;
			Token newRefreshToken;

			if (!accessTokenValid && !refreshTokenValid) {
				newAccessToken = jwtTokenProvider.generateToken(user);
				newRefreshToken = jwtTokenProvider.generateRefreshToken(user);
				addAccessTokenCookie(responseHeaders, newAccessToken);
				addRefreshTokenCookie(responseHeaders, newRefreshToken);
			}

			if (!accessTokenValid && refreshTokenValid){
				newAccessToken  = jwtTokenProvider.generateToken(user);
				addAccessTokenCookie(responseHeaders, newAccessToken);
			}

			if (accessTokenValid && refreshTokenValid){
				newAccessToken = jwtTokenProvider.generateToken(user);
				newRefreshToken = jwtTokenProvider.generateRefreshToken(user);
				addAccessTokenCookie(responseHeaders, newAccessToken);
				addRefreshTokenCookie(responseHeaders, newRefreshToken);
			}

			AuthResponse registerResponse = new AuthResponse(AuthResponse.Status.SUCCESS,"Register successful. Tokens are created in cookie.");

			return ResponseEntity.ok().headers(responseHeaders).body(registerResponse);
		}
		else{

			AuthResponse failregisterResponse = new AuthResponse(AuthResponse.Status.FAILURE,"Register failure. Username alredy exists");
			return ResponseEntity.ok().body(failregisterResponse);
		}
       
    }

}
