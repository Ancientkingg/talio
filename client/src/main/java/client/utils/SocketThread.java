
package client.utils;


import client.models.BoardModel;
import client.scenes.MainCtrl;
import lombok.Getter;

import lombok.Setter;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.*;

public class SocketThread implements  Runnable {

    @Getter @Setter
    private static String server = "ws://localhost:8080";

    @Getter
    private SessionHandler sessionHandler;

    /**
     * Constructor that instantiates the session handler the socket will use
     */
    public SocketThread() {
        sessionHandler = new SessionHandler();
    }

    /**
     * Thread function to run
     * Creates socket with sessionHandler
     */
    @Override public void run() {

        final WebSocketClient webSocketClient = new StandardWebSocketClient();
        final WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);

        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        stompClient.connect(server + "/greeting", sessionHandler);

        new Scanner(System.in).nextLine(); // Don't close immediately.
    }
}
