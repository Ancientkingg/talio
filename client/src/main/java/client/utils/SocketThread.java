
package client.utils;


import client.services.BoardService;
import client.services.ServerService;
import lombok.Getter;


import org.springframework.messaging.converter.CompositeMessageConverter;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.net.URI;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class SocketThread implements  Runnable {

    private String server;

    private final AtomicBoolean runningFlag;


    @Getter
    private SessionHandler sessionHandler;

    /**
     * Sets up socket parameters to connect to server
     * @param serverService ServerService is passed on to the sessionHandler
     * @param serverIP String the ip is changed to the right format
     * @param boardService BoardService that is passed to SessionHandler
     */
    public SocketThread(final ServerService serverService, final URI serverIP, final BoardService boardService) {
        sessionHandler = new SessionHandler(serverService, boardService);
        server = "ws://" + serverIP.getHost() + ":" + serverIP.getPort() + "/greeting";
        runningFlag = new AtomicBoolean(false);
    }

    /**
     * Stops the thread
     */
    public void stop() {
        runningFlag.set(false);
    }

    /**
     * Thread function to run
     * Creates socket with sessionHandler
     */
    @Override public void run() {

        final WebSocketClient webSocketClient = new StandardWebSocketClient();
        final WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);

        final List<MessageConverter> converters = new ArrayList<MessageConverter>();
        converters.add(new MappingJackson2MessageConverter()); // used to handle json messages
        converters.add(new StringMessageConverter()); // used to handle raw strings

        stompClient.setMessageConverter(new CompositeMessageConverter(converters));

        stompClient.connect(server, sessionHandler);

        while (true) { }
    }
}
