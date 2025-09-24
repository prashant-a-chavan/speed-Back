package com.project.speedBack.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // Messages will be sent to clients subscribing to '/topic'
        config.setApplicationDestinationPrefixes("/app"); // Clients send messages to '/app' endpoints
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register the '/ws' endpoint, enabling SockJS fallback for browsers that don't support WebSockets
        registry.addEndpoint("/ws").setAllowedOriginPatterns("http://localhost:3000").withSockJS();
    }
}