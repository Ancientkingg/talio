package client.scenes.components.modals;

import client.Main;
import client.scenes.OverviewCtrl;
import client.scenes.components.UIComponent;
import client.services.BoardService;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class BoardPasswordModal extends Modal implements UIComponent {
    @FXML
    private Button crossButton;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Text titleText;

    @FXML
    private Text incorrectPasswordText;

    private final OverviewCtrl parentCtrl;

    /**
     * Constructor for BoardPasswordModal
     * @param boardService boardService instance
     * @param parentScene parent scene (displayed under modal)
     * @param parentCtrl parent controller
     */
    public BoardPasswordModal(final BoardService boardService, final Scene parentScene, final OverviewCtrl parentCtrl) {
        super(boardService, parentScene);
        this.parentCtrl = parentCtrl;

        loadSource(Main.class.getResource("/components/BoardPasswordModal.fxml"));
    }

    /**
     * sets incorrectPasswordText visibility to false every time modal is opened to clear any old message
     */
    @Override
    public void initialize() {
        super.initialize();
        incorrectPasswordText.setVisible(false);

        if (OverviewCtrl.isLocked()) {
            titleText.setText("Unlock board");
        } else {
            titleText.setText("Lock board");
            passwordTextField.setText(boardService.getCurrentBoard().getPassword());
        }
    }

    /**
     * overriden closeModal method that also refreshes Homepage to reflect any changes made
     */
    @FXML
    @Override
    public void closeModal() {
        super.closeModal();
        parentCtrl.refresh();
    }

    /**
     * Shows modal in the parentScene
     */
    @FXML
    private void enableWriteAccess() {
        final String password = this.passwordTextField.getText();
        if (password == null || password.isEmpty()) {
            incorrectPasswordText.setText("This field cannot be left empty");
            incorrectPasswordText.setVisible(true);
            return;
        }

        if (OverviewCtrl.isLocked()) {
            if (boardService.getCurrentBoard().getPassword().equals(password)) {
                OverviewCtrl.setLocked(false);
                boardService.setLocalPasswordForCurrentBoard(password);
                closeModal();
            } else {
                incorrectPasswordText.setText("Entered password is incorrect");
                incorrectPasswordText.setVisible(true);
            }
        } else {
            OverviewCtrl.setLocked(true);
            boardService.setLocalPasswordForCurrentBoard(password);
            boardService.setPasswordForCurrentBoard(password);
            closeModal();
        }
    }
}
