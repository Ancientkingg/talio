package client.utils;

import client.exceptions.BoardChangeException;
import client.services.BoardService;
import client.services.ServerService;
import commons.Card;
import commons.Column;
import javafx.application.Platform;
import javafx.util.Pair;
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
    private List<Subscription> subscriptions;

    private final ServerService serverService;

    private final BoardService boardService;


    /**
     * Constructor to pass serverService in. This allows the handler to pass itself to the serverService after it is connected
     *
     * @param serverService ServerService to manage sessionHandler
     * @param boardService  BoardService used to manage client boardModel and UI
     */
    public SessionHandler(final ServerService serverService, final BoardService boardService) {
        this.serverService = serverService;
        this.boardService = boardService;
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
        for (final Subscription subscription : subscriptions) subscription.unsubscribe();
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
                    Platform.runLater( () -> {
                        boardService.updateRenameBoard((String) payload);
                        logger.info("Board renamed: " + payload.toString());
                    }); }
            });
        subscriptions.add(boardRenameSub);
    }

    private void subscribeToCardUpdates(final String joinKey) {
//        final Subscription cardRepositionedSub = session.subscribe(
//            "/topic/cards" + joinKey + "/reposition", new StompSessionHandlerAdapter() {
//                public Type getPayloadType(final StompHeaders headers) { return (new ImmutableTriple<String, Integer, Card>(null, null, null).getClass()); }
//
//                public void handleFrame(final StompHeaders headers, final Object payload) { Platform.runLater( () -> {
//                    logger.info("Card repositioned: " + payload.toString());
//                }); }
//            });
//        subscriptions.add(cardRepositionedSub);

//        final Subscription cardEditedSub = session.subscribe(
//            "/topic/cards" + joinKey + "/edit", new StompSessionHandlerAdapter() {
//                public Type getPayloadType(final StompHeaders headers) { return (new Pair<String, Card>(null, null)).getClass(); }
//
//                public void handleFrame(final StompHeaders headers, final Object payload) {
//                    logger.info("Card edited: " + payload.toString());
//                }
//            });
//        subscriptions.add(cardEditedSub);
        final Subscription cardAddedSub = session.subscribe(
            "/topic/cards" + joinKey + "/add", new StompSessionHandlerAdapter() {
                public Type getPayloadType(final StompHeaders headers) { return Pair.class; }

                public void handleFrame(final StompHeaders headers, final Object payload) {
                    Platform.runLater( () -> {
                        final Pair<Long, Card> pair = (Pair) payload;
                        final Column column = boardService.getCurrentBoard().getColumnById(pair.getKey());
                        try { boardService.updateAddCardToColumn(pair.getValue(), column); }
                        catch (BoardChangeException e) { throw new RuntimeException(e); }
                        logger.info("Card added: " + payload);
                    }); }
            });
        subscriptions.add(cardAddedSub);

        final Subscription cardRemovedSub = session.subscribe(
            "/topic/cards" + joinKey + "/remove", new StompSessionHandlerAdapter() {
                public Type getPayloadType(final StompHeaders headers) { return Pair.class; }

                public void handleFrame(final StompHeaders headers, final Object payload) {
                    Platform.runLater( () -> {
                        final Pair<Long, Card> pair = (Pair) payload;
                        final Column column = boardService.getCurrentBoard().getColumnById(pair.getKey());
                        try { boardService.updateRemoveCardFromColumn(pair.getValue(), column); }
                        catch (BoardChangeException e) { throw new RuntimeException(e); }
                        logger.info("Card added: " + payload.toString());
                    }); }
            });
        subscriptions.add(cardRemovedSub);
    }

    private void subscribeToColumnUpdates(final String joinKey) {
        final Subscription columnAddedSub = session.subscribe(
            "/topic/columns/" + joinKey + "/add", new StompSessionHandlerAdapter()  {
                public Type getPayloadType(final StompHeaders headers) {  return Column.class; }

                public void handleFrame(final StompHeaders headers, final Object payload) {
                    Platform.runLater(() -> {
                        final Column column = (Column) payload;
                        try { boardService.updateAddColumnToCurrentBoard(column); }
                        catch (BoardChangeException e) { throw new RuntimeException(e); }
                        logger.info("Column added: " + ((Column) payload).getHeading());
                    }); }
            });
        subscriptions.add(columnAddedSub);

//        final Subscription columnRenamedSub = session.subscribe(
//            "/topic/columns/" + joinKey + "/rename", new StompSessionHandlerAdapter() {
//                public Type getPayloadType(final StompHeaders headers) { return (new Pair<String, String>(null, null)).getClass(); }
//
//                public void handleFrame(final StompHeaders headers, final Object payload) {
//                    logger.info("Column renamed: " + payload.toString());
//                }
//            });
//        subscriptions.add(columnRenamedSub);

        final Subscription columnRemovedSub = session.subscribe(
            "/topic/columns/" + joinKey + "/remove", new StompSessionHandlerAdapter() {
                public Type getPayloadType(final StompHeaders headers) { return Long.class; }

                public void handleFrame(final StompHeaders headers, final Object payload) {
                    Platform.runLater(() -> {
                        try {  boardService.updateRemoveColumnFromCurrentBoard((Long) payload); }
                        catch (BoardChangeException e) { throw new RuntimeException(e); }
                        logger.info("Column removed");
                    }); }
            });
        subscriptions.add(columnRemovedSub);
    }

}
