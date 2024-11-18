package com.webchat_agc.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Slf4j
@Configuration
@RequiredArgsConstructor
@Profile({"!dev && !qa && !prod"})
@EnableMongoRepositories(basePackages = "co.my.data.repositories")
public class LocalDevMongoConfig extends AbstractMongoClientConfiguration {
    
    private final MongoConnectConfig config;
    
    @Override
    public String getDatabaseName() {
        return config.getDatabase();
    }
    
    @Override
    protected void configureClientSettings(MongoClientSettings.Builder builder) {
        log.info("Applying Local Dev MongoDB Configuration");
        builder.applyConnectionString(new ConnectionString(getConnectionString()));
    }

    //mongodb://${mongo.user}:${mongo.password}@${mongo.host}:${mongo.port}/${mongo.database}?authSource=admin
    private String getConnectionString() {
        return String.format("mongodb://%s:%s@%s:%s/%s??ssl=true&replicaSet=rs0&readPreference=secondaryPreferred&retryWrites=false",
                config.getUser(),
                config.getPassword(),
                config.getHost(),
                config.getPort(),
                config.getDatabase()
        );
    }
}
