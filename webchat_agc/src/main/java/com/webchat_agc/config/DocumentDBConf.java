package com.webchat_agc.config;

import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.autoconfigure.mongo.MongoPropertiesClientSettingsBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.MongoClientSettings;

@Configuration
public class DocumentDBConf {
	
	private MongoProperties properties;
	

        public DocumentDBConf(final MongoProperties properties) {
            super();
            this.properties = properties;
        }

        @Bean
        public MongoClientSettings mongoClientSettings() { 
            
	     return MongoClientSettings.builder()
                    .applyToSslSettings(builder -> builder.enabled(true))
                    .build();
	}

    @Bean
        public MongoPropertiesClientSettingsBuilderCustomizer mongoPropertiesCustomizer(final MongoProperties properties) {
			return new MongoPropertiesClientSettingsBuilderCustomizer(properties);
	}
}