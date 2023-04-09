package client.scenes.components;

import client.scenes.Refreshable;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import lombok.Getter;

import java.util.List;

public abstract class Draggable extends GridPane {

    @Getter
    private boolean draggable = false;

    private boolean dragState;

    private Refreshable parentController = null;

    private final Parent parentContainer;

    protected Node intersectedComponent;

    private boolean isBelow;

    private Draggable clone;


    private final EventHandler<MouseEvent> onPress = e -> {
        if (!e.isShiftDown()) return;
        dragState = true;

        final Scene rootScene = this.getScene();
        final StackPane root = (StackPane) rootScene.getRoot();

        this.clone = this.clone();

        clone.setMaxWidth(this.getWidth());
        clone.setPrefWidth(this.getWidth());
        clone.setMinWidth(this.getWidth());
        clone.setPickOnBounds(false);
        clone.setMouseTransparent(true);


        final Point2D p = this.localToScene(this.getWidth() / 2.0,this.getHeight() / 2.0);

        clone.setTranslateX(p.getX() - rootScene.getWidth() / 2.0);
        clone.setTranslateY(p.getY() - rootScene.getHeight() / 2.0);

        root.getChildren().add(clone);
    };

    private final EventHandler<MouseEvent> onDrag = e -> {
        if (!dragState) return;
        clone.setRotate(7.5);
        final Scene rootScene = this.getScene();
        this.setVisible(false);

        clone.setTranslateX(e.getSceneX() - rootScene.getWidth() / 2.0);
        clone.setTranslateY(e.getSceneY() - rootScene.getHeight() / 2.0);

        final Node intersectedNode = clone.pick(rootScene.getRoot(), e.getSceneX(), e.getSceneY());
        final Node intersectedComponent = clone.toComponent(intersectedNode);
        if (intersectedComponent == null) return;
        this.intersectedComponent = intersectedComponent;

        final Bounds b = intersectedNode.localToScene(intersectedNode.getBoundsInLocal());

        this.isBelow = e.getSceneY() > b.getCenterY();
        this.duringDrag(intersectedComponent, isBelow);
    };

    private final EventHandler<MouseEvent> onRelease = e -> {
        if (!dragState) return;
        dragState = false;
        if (this.getParent() instanceof StackPane)
            ((StackPane) clone.getParent()).getChildren().remove(clone);

        this.onDrop(intersectedComponent, isBelow);
        parentController.refresh();
    };

    /**
     * Constructor for Draggable
     * @param parentController Parent controller
     * @param parentContainer Parent container
     */
    public Draggable(final Refreshable parentController, final Parent parentContainer) {
        this.parentController = parentController;
        this.parentContainer = parentContainer;
        this.draggable = false;
        this.dragState = false;
    }


    /**
     * Sets whether the component is draggable or not
     * @param draggable True if draggable, false otherwise
     */
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

    /**
     * Called when the component is dropped
     * @param intersectedComponent Component that was intersected: either a Draggable or of the same class as the parent container
     * @param isBelow True if the component was dropped below the intersected component, false otherwise
     */
    protected abstract void onDrop(Node intersectedComponent, boolean isBelow);

    protected abstract void duringDrag(Node intersectedComponent, boolean isBelow);

    /**
     * Clones the component
     * @return Cloned component
     */
    public abstract Draggable clone();

    private Node toComponent(final Node node) {
        if (node == null) return null;
        if (node.getClass() == this.parentContainer.getClass()) return node;
        if (node instanceof Refreshable) return node;
        Node nodeCandidate = node;

        while (!(nodeCandidate instanceof Draggable) && !(nodeCandidate.getClass() == this.parentContainer.getClass())) {
            nodeCandidate = nodeCandidate.getParent();
            if (nodeCandidate == null) return null;
        }

        return nodeCandidate;
    }

    private Node pick(final Node node, final double sceneX, final double sceneY) {
        Point2D point = node.sceneToLocal(sceneX, sceneY, true);

        if (!node.contains(point)) return null;

        if (node instanceof Parent) {
            Node childCandidate = null;
            final List<Node> children = ((Parent) node).getChildrenUnmodifiable();

            for (int i = children.size() - 1; i >= 0; i--) {
                final Node child = children.get(i);

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
