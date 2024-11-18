package com.webchat_agc.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.connection.netty.NettyStreamFactoryFactory;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;

@Slf4j
@Configuration
public class MongoDbConfiguration {

    private static final String AWS_PUBLIC_KEY_NAME = "global-bundle.pem";

    private final String mongoConnectionUri;

    private final String databaseName;

    public MongoDbConfiguration(@Value("${spring.data.mongodb.uri}") String mongoConnectionUri, @Value("${spring.data.mongodb.database}") String databaseName) {
        this.mongoConnectionUri = mongoConnectionUri;
        this.databaseName = databaseName;
    }


    @Bean
    @Primary
    @SneakyThrows
    @Profile("!default")
    public MongoClient mongoClient() {

        SslContext sslContext = SslContextBuilder.forClient()
                .sslProvider(SslProvider.OPENSSL)
                .trustManager(new ClassPathResource(AWS_PUBLIC_KEY_NAME).getInputStream())
                .build();

        ConnectionString connectionString = new ConnectionString(mongoConnectionUri);

        return MongoClients.create(
                MongoClientSettings.builder()
                        .applyConnectionString(connectionString)
                        .applyToSslSettings(builder -> {
                            builder.enabled((null == connectionString.getSslEnabled()) ? false : connectionString.getSslEnabled());
                            builder.invalidHostNameAllowed((null == connectionString.getSslInvalidHostnameAllowed()) ? false : connectionString.getSslInvalidHostnameAllowed());
                        })
                        .streamFactoryFactory(NettyStreamFactoryFactory.builder()
                                .sslContext(sslContext)
                                .build())
                        .build());
    }
}