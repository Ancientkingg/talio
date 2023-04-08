package client.scenes.components;

import client.scenes.Refreshable;
import javafx.animation.PauseTransition;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import lombok.Getter;

import java.util.List;

public abstract class Draggable extends GridPane {

    @Getter
    private boolean draggable;

    private boolean dragState;

    private Refreshable parentContainer = null;

    private Draggable intersectedComponent;

    private boolean isBelow;


    private final EventHandler<MouseEvent> onPress = e -> {
        if (!e.isShiftDown()) return;
        dragState = true;

        Scene rootScene = this.getScene();
        StackPane root = (StackPane) rootScene.getRoot();

        this.setMaxWidth(this.getWidth());
        this.setPrefWidth(this.getWidth());
        this.setMinWidth(this.getWidth());
        this.setPickOnBounds(false);
        this.setMouseTransparent(true);


        Point2D p = this.localToScene(this.getWidth()/2.0,this.getHeight()/2.0);

        this.setTranslateX(p.getX() - rootScene.getWidth()/2.0);
        this.setTranslateY(p.getY() - rootScene.getHeight()/2.0);

        root.getChildren().add(this);
    };

    private final EventHandler<MouseEvent> onDrag = e -> {
        if (!dragState) return;
        this.setRotate(7.5);
        Scene rootScene = this.getScene();

        this.setTranslateX(e.getSceneX() - rootScene.getWidth()/2.0);
        this.setTranslateY(e.getSceneY() - rootScene.getHeight()/2.0);

        Node intersectedNode = pick(rootScene.getRoot(), e.getSceneX(), e.getSceneY());
        Draggable intersectedComponent = toDraggableComponent(intersectedNode);
        if (intersectedComponent == null) return;
        this.intersectedComponent = intersectedComponent;

        Bounds b = intersectedNode.localToScene(intersectedNode.getBoundsInLocal());

        this.isBelow = e.getSceneY() > b.getCenterY();
    };

    private final EventHandler<MouseEvent> onRelease = e -> {
        if (!dragState) return;
        dragState = false;
        if (this.getParent() instanceof StackPane)
            ((StackPane) this.getParent()).getChildren().remove(this);

        this.onDrop(intersectedComponent, isBelow);
        parentContainer.refresh();
    };

    public Draggable(Refreshable parentContainer) {
        this.parentContainer = parentContainer;
        this.draggable = false;
        this.dragState = false;
    }


    public void setDraggable(final boolean draggable) {
        this.draggable = draggable;
        if (draggable) {
            this.addEventHandler(MouseEvent.MOUSE_PRESSED, onPress);
            this.addEventHandler(MouseEvent.MOUSE_DRAGGED, onDrag);
            this.addEventHandler(MouseEvent.MOUSE_RELEASED, onRelease);
        } else {
            this.removeEventHandler(MouseEvent.MOUSE_PRESSED, onPress);
            this.removeEventHandler(MouseEvent.MOUSE_DRAGGED, onDrag);
            this.removeEventHandler(MouseEvent.MOUSE_RELEASED, onRelease);
        }
    }

    private void addPressAndHoldEventHandler(Duration holdTime,
                                        EventHandler<MouseEvent> handler) {

        class Wrapper<T> { T content ; }
        Wrapper<MouseEvent> eventWrapper = new Wrapper<>();

        PauseTransition holdTimer = new PauseTransition(holdTime);
        holdTimer.setOnFinished(event -> handler.handle(eventWrapper.content));


        this.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            eventWrapper.content = event;
            holdTimer.playFromStart();
        });
        this.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> holdTimer.stop());
        this.addEventHandler(MouseEvent.DRAG_DETECTED, event -> holdTimer.stop());
    }

    public abstract void onDrop(Draggable intersectedComponent, boolean isBelow);

    private Draggable toDraggableComponent(final Node node) {
        if (node == null) return null;
        if (node instanceof Draggable) return (Draggable) node;
        Node nodeCandidate = node;

        while (!(nodeCandidate instanceof Draggable)) {
            nodeCandidate = nodeCandidate.getParent();
            if (nodeCandidate == null) return null;
        }

        return (Draggable) nodeCandidate;
    }

    private Node pick(final Node node, final double sceneX, final double sceneY) {
        Point2D point = node.sceneToLocal(sceneX, sceneY, true);

        if (!node.contains(point)) return null;

        if (node instanceof Parent) {
            Node childCandidate = null;
            List<Node> children = ((Parent) node).getChildrenUnmodifiable();

            for (int i = children.size() - 1; i >= 0; i--) {
                Node child = children.get(i);

                point = child.sceneToLocal(sceneX, sceneY, true);

                if (!child.isMouseTransparent() && child.contains(point)) {
                    childCandidate = child;
                    break;
                }
            }

            if (childCandidate != null) {
                return this.pick(childCandidate, sceneX, sceneY);
            }
        }

        return node;
    }
}
