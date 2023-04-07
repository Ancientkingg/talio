package client.scenes.components;

import client.Main;
import client.exceptions.BoardChangeException;
import client.scenes.HomePageCtrl;
import client.services.BoardService;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.HashSet;
import java.util.TreeSet;

public class CreateBoardModal extends Modal implements UIComponent {

    @FXML
    private Button crossButton;

    @FXML
    private TextField titleTextField;

    @FXML
    private TextField passwordTextField;

    private final HomePageCtrl parentCtrl;

    /**
     * Constructor for CreateBoardModal
     * @param boardService boardService instance
     * @param parentScene parent scene (displayed under modal)
     * @param parentCtrl parent controller
     */
    public CreateBoardModal(final BoardService boardService, final Scene parentScene, final HomePageCtrl parentCtrl) {
        super(boardService, parentScene);
        this.parentCtrl = parentCtrl;

        loadSource(Main.class.getResource("/components/CreateBoardModal.fxml"));
    }

    /**
     * Closes modal
     */
    @FXML
    public void closeModal() {
        super.closeModal();
    }

    @FXML
    private void createBoard() throws BoardChangeException {
        final String title = titleTextField.getText();
        final String password = passwordTextField.getText();
        final String p = password.isBlank() ? null : password;

        final Board serverBoard = boardService.addBoard(new Board("join-key", title, p, new TreeSet<>(), new HashSet<>(0)));
        super.closeModal();
        parentCtrl.loadBoard(serverBoard);
    }
}
