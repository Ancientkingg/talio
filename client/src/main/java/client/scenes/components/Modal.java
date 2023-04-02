package client.scenes.components;

import client.services.BoardService;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Modal extends GridPane {

    protected final BoardService boardService;

    protected final Scene parentScene;

    private final EventHandler<MouseEvent> eventFilter;


    /**
     * Default constructor for modal
     * @param boardService boardService instance
     * @param parentScene scene behind modal
     */
    public Modal(final BoardService boardService, final Scene parentScene) {
        this.boardService = boardService;
        this.parentScene = parentScene;

        this.eventFilter = event -> {
            if (!inHierarchy(event.getPickResult().getIntersectedNode(), this)) {
                closeModal();
            }
        };
    }

    /**
     * Is run upon initialization of modal
     */
    @FXML
    public void initialize() {
        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(() -> parentScene.addEventFilter(MouseEvent.MOUSE_CLICKED, eventFilter), 150, TimeUnit.MILLISECONDS);
    }

    /**
     * Closes modal
     */
    public void closeModal() {
        ((StackPane) parentScene.getRoot()).getChildren().remove(this);
        parentScene.removeEventFilter(MouseEvent.MOUSE_CLICKED, eventFilter);
    }

    /**
     * Shows modal in the parentScene
     */
    public void showModal() {
        ((StackPane) parentScene.getRoot()).getChildren().add(this);
    }

    /**
     * Checks if node is in the hierarchy of potentialHierarchyElement
     * @param node node to check
     * @param potentialHierarchyElement potential hierarchy element
     * @return true if node is in the hierarchy of potentialHierarchyElement
     */
    private boolean inHierarchy(final Node node, final Node potentialHierarchyElement) {
        if (potentialHierarchyElement == null) {
            return true;
        }
        Node nodeTraverser = node;
        while (nodeTraverser != null) {
            if (nodeTraverser == potentialHierarchyElement) {
                return true;
            }
            nodeTraverser = nodeTraverser.getParent();
        }
        return false;
    }
}

