package com.webchat_agc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "amazonmq")
public class AmazonMQProperties {

    private String relayHost;
    private int relayPort;
    private String user;
    private String password;
}