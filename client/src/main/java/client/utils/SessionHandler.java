package client.utils;

import client.models.BoardModel;
import client.scenes.MainCtrl;
import client.services.ServerService;
import commons.Card;
import commons.Column;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSession.Subscription;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.lang.reflect.Type;

@Component
public class SessionHandler extends StompSessionHandlerAdapter {

    private Logger logger = LogManager.getLogger(SessionHandler.class);

    private StompSession session;
    private ServerService serverService;

    private Subscription cardSubscription;
    private Subscription columnSubscription;

    public SessionHandler(final ServerService serverService) {
        this.serverService = serverService;
    }

    /**
     * After the socket connects with the server the session is saved
     * and this, the session handler, is passed to mainCtrl
     * @param session the client STOMP session
     * @param headers the STOMP CONNECTED frame headers
     */
    @Override
    public void afterConnected(@Nullable final StompSession session, @Nullable final StompHeaders headers) {
        this.session = session;
        serverService.setSession(session);
    }

    /**
     * Subscribes socket to the board associated with joinKey and unsubscribes from previous boards
     * if there were any.
     * @param joinKey String key to subscribe to
     */
    public void subscribeToBoard(final String joinKey) {
        // Unsubscribes from previous board, so that unnecessary traffic is avoided
        if (cardSubscription != null) {
            cardSubscription.unsubscribe();
        }
        if (columnSubscription != null) {
            columnSubscription.unsubscribe();
        }

        final Subscription cardSubscription = session.subscribe("/topic/" + joinKey + "/cards", new StompSessionHandlerAdapter() {
            @Override
            public Type getPayloadType(@Nullable final StompHeaders headers) {
                return Card.class;
            }

            @Override
            public void handleFrame(@Nullable final StompHeaders headers, final @Nullable Object payload) {
                // Add any method here that you wish to execute on the payload
                logger.info(payload);
            }
        });
        logger.info("Subscribed to /topic/" + joinKey + "/cards");

        final Subscription columnSubscription = session.subscribe("/topic/" + joinKey + "/columns", new StompSessionHandlerAdapter() {
            @Override
            public Type getPayloadType(@Nullable final StompHeaders headers) {
                return Column.class;
            }

            @Override
            public void handleFrame(@Nullable final StompHeaders headers, final @Nullable Object payload) {
                // Add any method here that you wish to execute on the payload
                logger.info(payload);
            }
        });
        logger.info("Subscribed to /topic/" + joinKey + "/columns");

        this.cardSubscription = cardSubscription;
        this.columnSubscription = columnSubscription;
    }
}
