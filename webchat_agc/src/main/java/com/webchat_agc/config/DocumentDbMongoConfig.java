package com.webchat_agc.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.stream.Collectors;

// @Slf4j
// @Configuration
// @RequiredArgsConstructor  
// //@Profile({"dev || qa || prod"})
// @EnableMongoRepositories(basePackages = "co.my.data.repositories")
// public class DocumentDbMongoConfig extends AbstractMongoClientConfiguration {

//     private final MongoConnectConfig config;

//     @Override
//     public String getDatabaseName() {
//         return config.getDatabase();
//     }

//     @SneakyThrows
//     @Override
//     protected void configureClientSettings(MongoClientSettings.Builder builder) {
//         log.info("Applying AWS DocumentDB Configuration");
//         builder.applyConnectionString(new ConnectionString(getConnectionString()));
//         var endOfCertificateDelimiter = "-----END CERTIFICATE-----";
//         File resource = new ClassPathResource("certs/global-bundle.pem").getFile();
//         String pemContents = new String(Files.readAllBytes(resource.toPath()));
//         var allCertificates = Arrays.stream(pemContents
//                 .split(endOfCertificateDelimiter))
//                 .filter(line -> !line.isBlank())
//                 .map(line -> line + endOfCertificateDelimiter)
//                 .collect(Collectors.toUnmodifiableList());


//         var certificateFactory = CertificateFactory.getInstance("X.509");
//         var keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
//         // This allows us to use an in-memory key-store
//         keyStore.load(null);

//         for (int i = 0; i < allCertificates.size(); i++) {
//             var certString = allCertificates.get(i);
//             var caCert = certificateFactory.generateCertificate(new ByteArrayInputStream(certString.getBytes()));
//             keyStore.setCertificateEntry(String.format("AWS-certificate-%s", i), caCert);
//         }

//         var trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//         trustManagerFactory.init(keyStore);

//         var sslContext = SSLContext.getInstance("TLS");
//         sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

//         builder.applyToSslSettings(ssl -> {
//             ssl.enabled(true).context(sslContext);
//         });
//     }

//     /**
//      * Partly based on the AWS Console "Connectivity & security " section in the DocumentDB Cluster View.
//      *   Since we register the pem above, we don't need to add the ssl & sslCAFile piece
//      *   mongodb://${user}:${password}@${host}:${port}/?replicaSet=rs0&readPreference=secondaryPreferred&retryWrites=false
//      */
//     private String getConnectionString() {
//         return String.format("mongodb://%s:%s@%s:%s/%s&readPreference=secondaryPreferred&retryWrites=false&connectTimeoutMS=30000&socketTimeoutMS=30000&retryWrites=false",
//                 config.getUser(),
//                 config.getPassword(),
//                 config.getHost(),
//                 config.getPort(),
//                 config.getDatabase()
//         );
//     }
// }