package client.scenes.components;

import client.Main;
import client.scenes.HomePageCtrl;
import commons.Board;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;

public class BoardCardComponent extends Pane {

    private final Board board;

    private final HomePageCtrl parentCtrl;

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
}
