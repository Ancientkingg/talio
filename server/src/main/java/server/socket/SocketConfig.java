/**
 * Implemented based on this guide: https://www.baeldung.com/spring-websockets-sendtouser
 */

package server.socket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class SocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Configures server socket handler with paths
     * @param config Registry to configure
     */
    public void configureMessageBroker(final MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic/");
        config.setApplicationDestinationPrefixes("/app/");
    }

    /**
     * Configures server socket handler with path for greeting
     * @param registry Registry to configure
     */
    public void registerStompEndpoints(final StompEndpointRegistry registry) {
        registry.addEndpoint("/greeting");
    }
}
