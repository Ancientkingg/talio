package client.scenes.components;

import client.Main;
import client.scenes.HomePageCtrl;
import commons.Board;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class BoardCardComponent extends Pane implements UIComponent {

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

        loadSource(Main.class.getResource("/components/BoardCard.fxml"));

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
     * Enables/disables admin controls - deleting boards.
     * Also disables the leave button since leaving only removes the board from the homepage but the board persists in the server.
     * This goes against the requirement - the admin should be able to see all boards on the server.
     */
    public void changeMode() {

        final boolean isAdmin = Main.isAdmin();

        deleteButton.setVisible(isAdmin); // if admin, make visible
        deleteButton.setDisable(!isAdmin); // if admin, enable

        leaveButton.setVisible(!isAdmin); // if admin, hide leave button
        leaveButton.setDisable(isAdmin); // if admin, disable leave button
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
