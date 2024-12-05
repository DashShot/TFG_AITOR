package com.webchat_agc.config;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompReactorNettyCodec;
import org.springframework.messaging.tcp.reactor.ReactorNettyTcpClient;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webchat_agc.security.jwt.JwtChannelInterceptor;

import reactor.netty.tcp.SslProvider;


@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private final AmazonMQProperties amazonMQProperties;

    public WebSocketConfig(AmazonMQProperties activeMQProperties) {
        this.amazonMQProperties = activeMQProperties;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        // Configura el cliente TCP con Reactor Netty para Amazon MQ
        ReactorNettyTcpClient<byte[]> tcpClient = new ReactorNettyTcpClient<>(builder -> builder
        .host(amazonMQProperties.getRelayHost())  // Host del broker de Amazon MQ
        .port(amazonMQProperties.getRelayPort())  // Puerto del broker
        .secure(SslProvider.defaultClientProvider())  // Configuración SSL
        , new StompReactorNettyCodec());  // Código de codificación de STOMP

        // Configuración del Message Broker para STOMP
        registry.setApplicationDestinationPrefixes("/app"); // Prefijo para los destinos de la aplicación
        registry.enableStompBrokerRelay("/queue", "/topic")  // Prefijos para el broker
            .setUserDestinationBroadcast("/topic/unresolved.user.dest")
            .setUserRegistryBroadcast("/topic/registry.broadcast")
            .setAutoStartup(true)
            .setSystemLogin(amazonMQProperties.getUser()) // Usuario del sistema
            .setSystemPasscode(amazonMQProperties.getPassword()) // Contraseña del sistema
            .setClientLogin(amazonMQProperties.getUser()) // Usuario del cliente
            .setClientPasscode(amazonMQProperties.getPassword()) // Contraseña del cliente
            .setTcpClient(tcpClient); // Configuración del cliente TCP
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/api/socket")
                .setAllowedOriginPatterns("*")//CLiente Angular "https://localhost:4200/"
                .withSockJS()
                .setStreamBytesLimit(128 * 1024 * 1024);  // Aumentar el límite a 10 MB
                //.setHttpMessageCacheSize(1000)
                //.setDisconnectDelay(30 * 1000);
    }

    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        //JSON CONVERT
        try{
            DefaultContentTypeResolver resolver = new DefaultContentTypeResolver();
            resolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON);

            MappingJackson2MessageConverter  converter = new MappingJackson2MessageConverter();

            converter.setObjectMapper(new ObjectMapper());
            converter.setContentTypeResolver(resolver);
            messageConverters.add(converter);
 
        }catch(Exception e){
            System.err.println("TYPE NOT SUPPORTED");
        }
        return false;
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration){
        registration.setMessageSizeLimit(128 * 1024 * 1024); // Aumenta el límite a 128 MB (o más según lo necesario)
        registration.setSendBufferSizeLimit(128 * 1024 * 1024); // Aumenta el límite del buffer a 20 MB
        registration.setSendTimeLimit(60 * 1000); // Aumenta el límite de tiempo de envío a 60 segundos
    }

    //AUMENTO TAMAÑO Buffers
    @Bean
    public ServletServerContainerFactoryBean createServletServerContainerFactoryBean() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(20 * 1024 * 1024);
        container.setMaxBinaryMessageBufferSize(20 *1024  * 1024);

        return container;
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration){
        registration.interceptors(jwtChannelInterceptor());
    }

    @Bean
    public JwtChannelInterceptor jwtChannelInterceptor(){
        return new JwtChannelInterceptor();
    }

}
