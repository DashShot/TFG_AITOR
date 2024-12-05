package com.webchat_agc.webchat_agc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@SpringBootApplication
@EnableMongoRepositories( "com.webchat_agc.repositories")
@ComponentScan({"com.webchat_agc.config", "com.webchat_agc.services", "com.webchat_agc.controllers","com.webchat_agc.repositories", "com.webchat_agc.security"})
public class WebchatAgcApplication  {


	public static void main(String[] args) {

		SpringApplication.run(WebchatAgcApplication.class, args);
	
	}

}
