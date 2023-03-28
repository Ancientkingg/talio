package client.utils;

import client.services.ServerService;
import commons.Card;
import commons.Column;
import javafx.util.Pair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSession.Subscription;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Component
public class SessionHandler extends StompSessionHandlerAdapter {

    private Logger logger = LogManager.getLogger(SessionHandler.class);

    private StompSession session;
    private ServerService serverService;
    private List<Subscription> subscriptions;

    /**
     * Constructor to pass serverService in. This allows the handler to pass itself to the serverService after it is connected
     * @param serverService ServerService to manage sessionHandler
     */
    public SessionHandler(final ServerService serverService) {
        this.serverService = serverService;
        this.subscriptions = new ArrayList<>();
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
        serverService.setHandler(this);
    }

    /**
     * Subscribes socket to the board associated with joinKey and unsubscribes from previous boards
     * if there were any.
     * @param joinKey String key to subscribe to
     */
    public void subscribeToBoard(final String joinKey) {
        // Unsubscribes from previous board, so that unnecessary traffic is avoided
        subscriptions.clear();

        subscribeToBoardUpdates(joinKey);

        subscribeToColumnUpdates(joinKey);

        subscribeToCardUpdates(joinKey);

        logger.info("Subscribed to board: " + joinKey);
    }

    private void subscribeToBoardUpdates(final String joinKey) {
        final Subscription boardRenameSub = session.subscribe(
            "/topic/boards/" + joinKey + "/rename", new StompSessionHandlerAdapter() {
                public Type getPayloadType(final StompHeaders headers) {  return String.class; }

                public void handleFrame(final StompHeaders headers, final Object payload) {
                    logger.info("Board renamed: " + payload.toString());
                }
            });
        subscriptions.add(boardRenameSub);
    }

    private void subscribeToCardUpdates(final String joinKey) {
        final Subscription cardRepositionedSub = session.subscribe(
            "/topic/cards" + joinKey + "/reposition", new StompSessionHandlerAdapter() {
                public Type getPayloadType(final StompHeaders headers) { return (new ImmutableTriple<String, Integer, Card>(null, null, null).getClass()); }

                public void handleFrame(final StompHeaders headers, final Object payload) {
                    logger.info("Card repositioned: " + payload.toString());
                }
            });
        subscriptions.add(cardRepositionedSub);

        final Subscription cardEditedSub = session.subscribe(
            "/topic/cards" + joinKey + "/edit", new StompSessionHandlerAdapter() {
                public Type getPayloadType(final StompHeaders headers) { return (new Pair<String, Card>(null, null)).getClass(); }

                public void handleFrame(final StompHeaders headers, final Object payload) {
                    logger.info("Card edited: " + payload.toString());
                }
            });
        subscriptions.add(cardEditedSub);

        final Subscription cardAddedSub = session.subscribe(
            "/topic/cards" + joinKey + "/add", new StompSessionHandlerAdapter() {
                public Type getPayloadType(final StompHeaders headers) { return (new Pair<String, Card>(null, null)).getClass(); }

                public void handleFrame(final StompHeaders headers, final Object payload) {
                    logger.info("Card added: " + payload.toString());
                }
            });
        subscriptions.add(cardAddedSub);

        final Subscription cardRemovedSub = session.subscribe(
            "/topic/cards" + joinKey + "/remove", new StompSessionHandlerAdapter() {
                public Type getPayloadType(final StompHeaders headers) { return (new Pair<String, Card>(null, null)).getClass(); }

                public void handleFrame(final StompHeaders headers, final Object payload) {
                    logger.info("Card removed: " + payload.toString());
                }
            });
        subscriptions.add(cardRemovedSub);
    }

    private void subscribeToColumnUpdates(final String joinKey) {
        final Subscription columnAddedSub = session.subscribe(
            "/topic/columns/" + joinKey + "/add", new StompSessionHandlerAdapter() {
                public Type getPayloadType(final StompHeaders headers) {  return Column.class; }

                public void handleFrame(final StompHeaders headers, final Object payload) {
                    logger.info("Column added: " + payload.toString());
                }
            });
        subscriptions.add(columnAddedSub);

        final Subscription columnRenamedSub = session.subscribe(
            "/topic/columns/" + joinKey + "/rename", new StompSessionHandlerAdapter() {
                // I am unsure how to get the generic class type so I do this
                public Type getPayloadType(final StompHeaders headers) { return (new Pair<String, String>(null, null)).getClass(); }

                public void handleFrame(final StompHeaders headers, final Object payload) {
                    logger.info("Column renamed: " + payload.toString());
                }
            });
        subscriptions.add(columnRenamedSub);

        final Subscription columnRemovedSub = session.subscribe(
            "/topic/columns/" + joinKey + "/remove", new StompSessionHandlerAdapter() {
                public Type getPayloadType(final StompHeaders headers) { return Card.class; }

                public void handleFrame(final StompHeaders headers, final Object payload) {
                    logger.info("Column removed: " + payload.toString());
                }
            });
        subscriptions.add(columnRemovedSub);
    }

}
