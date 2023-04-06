package client.scenes.components;

import client.Main;
import client.scenes.HomePageCtrl;
import commons.Board;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;

public class BoardCardComponent extends Pane {

    private final Board board;

    private final HomePageCtrl parentCtrl;

    @FXML
    private Button leaveButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Text boardTitle;

    /**
     * Constructor for BoardCardComponent
     * @param board Board instance
     * @param parentCtrl HomePageCtrl instance
     */
    public BoardCardComponent(final Board board, final HomePageCtrl parentCtrl) {
        this.board = board;
        this.parentCtrl = parentCtrl;

        final FXMLLoader loader = new FXMLLoader(Main.class.getResource("/components/BoardCard.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        setBoardTitle();
        setHover();
        setClick();
    }

    private void setHover() {
        this.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                this.getStyleClass().add("hovered");
            } else {
                this.getStyleClass().remove("hovered");
            }
        });
    }

    private void setClick() {
        this.setOnMouseClicked(event -> onClick());
    }

    private void onClick() {
        parentCtrl.loadBoard(board);
    }

    private void setBoardTitle() {
        String boardTitleText = "Untitled Board";

        boardTitleText = board != null && board.getTitle() != null ? board.getTitle() : boardTitleText;
        boardTitle.setText(boardTitleText);
    }

    /**
     * Enables admin controls such as the delete board button
     */
    public void enableAdmin() {
        deleteButton.setVisible(true);
        deleteButton.setDisable(false);
    }

    @FXML
    private void onLeave() {
        parentCtrl.removeBoard(this.board);

        final Point2D p = leaveButton.localToScreen(-110, 32);

        final Tooltip customTooltip = new Tooltip("Left board!");
        customTooltip.setAutoHide(false);
        customTooltip.show(leaveButton,p.getX(),p.getY());

        final PauseTransition pt = new PauseTransition(Duration.millis(1250));
        pt.setOnFinished(e -> {
            customTooltip.hide();
        });
        pt.play();
    }

    @FXML
    private void onDelete() {
        parentCtrl.deleteBoard(this.board);
    }
}
