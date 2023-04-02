package client.scenes.components;

import client.Main;
import client.scenes.HomePageCtrl;
import client.services.BoardService;
import commons.Board;
import jakarta.ws.rs.NotFoundException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.List;

public class JoinBoardModal extends Modal {

    @FXML
    private Button crossButton;

    @FXML
    private TextField joinKeyTextField;

    private final HomePageCtrl parentCtrl;

    /**
     * Constructor for JoinBoardModal
     * @param boardService boardService instance
     * @param parentScene parent scene (displayed under modal)
     * @param parentCtrl parent controller
     */
    public JoinBoardModal(final BoardService boardService, final Scene parentScene, final HomePageCtrl parentCtrl) {
        super(boardService, parentScene);
        this.parentCtrl = parentCtrl;

        final FXMLLoader loader = new FXMLLoader(Main.class.getResource("/components/JoinBoardModal.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Closes modal
     */
    @FXML
    public void closeModal() {
        super.closeModal();
    }

    /**
     * Shows modal in the parentScene
     */
    @FXML
    private void joinBoard() {
        final String joinKey = this.joinKeyTextField.getText();
        if (joinKey != null && !joinKey.isEmpty()) {
            super.closeModal();

            try {
                final Board serverBoard = boardService.fetchBoard(joinKey);
                final List<Board> boards = boardService.getAllBoards();
                if (!boards.contains(serverBoard)) boards.add(serverBoard);

                parentCtrl.loadBoard(serverBoard);
            } catch (NotFoundException e) {
                System.out.println("Board not found!");
            }
        }
    }
}
