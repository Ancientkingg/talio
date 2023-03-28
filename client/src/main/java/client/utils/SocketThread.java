
package client.utils;


import client.services.ServerService;
import lombok.Getter;


import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.net.URI;
import java.util.*;

public class SocketThread implements  Runnable {

    private String server;

    @Getter
    private SessionHandler sessionHandler;

    /**
     * Sets up socket parameters to connect to server
     * @param serverService ServerService is passed on to the sessionHandler
     * @param serverIP String the ip is changed to the right format
     */
    public SocketThread(final ServerService serverService, final URI serverIP) {
        sessionHandler = new SessionHandler(serverService);
        server = "ws://" + serverIP.getHost() + ":" + serverIP.getPort() + "/greeting";
    }

    /**
     * Thread function to run
     * Creates socket with sessionHandler
     */
    @Override public void run() {

        final WebSocketClient webSocketClient = new StandardWebSocketClient();
        final WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);

        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        stompClient.connect(server, sessionHandler);

        while (true) { }
    }
}
