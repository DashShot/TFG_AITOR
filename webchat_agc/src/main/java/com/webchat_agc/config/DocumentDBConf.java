package com.webchat_agc.config;

import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.autoconfigure.mongo.MongoPropertiesClientSettingsBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.MongoClientSettings;

@Configuration
public class DocumentDBConf {
	
	private MongoProperties properties;
	
	// public static final String KEY_STORE_TYPE = "/tmp/certs/rds-truststore.jks";
    // public static final String DEFAULT_KEY_STORE_PASSWORD = "changeit";

    public DocumentDBConf(final MongoProperties properties) {
        super();
        this.properties = properties;
    }

    @Bean
    public MongoClientSettings mongoClientSettings() { 
            // setSslProperties();
	     return MongoClientSettings.builder()
                    .applyToSslSettings(builder -> builder.enabled(false))
                    .build();
	}

        // private static void setSslProperties() { 
    	//       System.setProperty("javax.net.ssl.trustStore", KEY_STORE_TYPE);
    	//       System.setProperty("javax.net.ssl.trustStorePassword",           
        //             DEFAULT_KEY_STORE_PASSWORD);
        // }

	@Bean
        public MongoPropertiesClientSettingsBuilderCustomizer mongoPropertiesCustomizer(final MongoProperties properties) {
			return new MongoPropertiesClientSettingsBuilderCustomizer(properties);
	}
}