package com.webchat_agc.webchat_agc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.webchat_agc.config", "com.webchat_agc.repositories", "com.webchat_agc.controllers"})
public class WebchatAgcApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebchatAgcApplication.class, args);
	}

}
