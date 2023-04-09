package client.scenes.components.modals;

import client.services.BoardService;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Modal extends GridPane {

    protected final BoardService boardService;

    protected final Scene parentScene;

    private Pane background;

    private final EventHandler<MouseEvent> eventFilter;


    /**
     * Default constructor for modal
     * @param boardService boardService instance
     * @param parentScene scene behind modal
     */
    public Modal(final BoardService boardService, final Scene parentScene) {
        this.boardService = boardService;
        this.parentScene = parentScene;

        setBackgroundPane();

        this.eventFilter = event -> closeModal();

        parentScene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode().equals(KeyCode.ESCAPE)) {
                closeModal();
                event.consume();
            }
        });
    }

    /**
     * Is run upon initialization of modal
     */
    @FXML
    public void initialize() {
        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(() -> this.background.addEventFilter(MouseEvent.MOUSE_CLICKED, eventFilter), 150, TimeUnit.MILLISECONDS);
    }

    /**
     * Closes modal
     */
    public void closeModal() {
        ((StackPane) parentScene.getRoot()).getChildren().remove(this);
        ((StackPane) parentScene.getRoot()).getChildren().remove(background);
        parentScene.removeEventFilter(MouseEvent.MOUSE_CLICKED, eventFilter);
    }

    /**
     * Shows modal in the parentScene
     */
    public void showModal() {
        ((StackPane) parentScene.getRoot()).getChildren().add(this);
    }

    private void setBackgroundPane() {
        this.background = new Pane();
        this.background.setStyle("-fx-background-color: rgba(0, 0, 0, 0.1);");
        this.background.setPrefSize(10000, 10000);
        this.background.setMinSize(10000, 10000);
        this.background.setMaxSize(10000, 10000);

        ((StackPane) parentScene.getRoot()).getChildren().add(background);
    }
}

