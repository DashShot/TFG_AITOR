package com.webchat_agc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "mongo")
public class MongoConnectConfig {

    private int port;

    private String host;

    private String user;

    private String database;

    private String password;

}
