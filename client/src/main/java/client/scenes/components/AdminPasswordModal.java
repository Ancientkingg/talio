package client.scenes.components;

import client.Main;
import client.scenes.HomePageCtrl;
import client.services.BoardService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;

public class AdminPasswordModal extends Modal {
    @FXML
    private Button crossButton;

    @FXML
    private TextField adminPasswordTextField;

    @FXML
    private Text incorrectPasswordText;

    private final HomePageCtrl parentCtrl;

    /**
     * Constructor for JoinBoardModal
     * @param boardService boardService instance
     * @param parentScene parent scene (displayed under modal)
     * @param parentCtrl parent controller
     */
    public AdminPasswordModal(final BoardService boardService, final Scene parentScene, final HomePageCtrl parentCtrl) {
        super(boardService, parentScene);
        this.parentCtrl = parentCtrl;

        final FXMLLoader loader = new FXMLLoader(Main.class.getResource("/components/AdminPasswordModal.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * sets incorrectPasswordText visibility to false every time modal is opened to clear any old message
     */
    @Override
    public void initialize() {
        super.initialize();
        incorrectPasswordText.setVisible(false);
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
    private void enableAdminMode() {

        final String adminPassword = this.adminPasswordTextField.getText();

        if (adminPassword != null && !adminPassword.isEmpty()) {

            if (parentCtrl.verifyAdminPassword(adminPassword)) {
                Main.setAdmin(true);
                closeModal();
                parentCtrl.loadAllBoards();
            }
            else {
                incorrectPasswordText.setText("Entered password is incorrect");
                incorrectPasswordText.setVisible(true);
            }
        }
        else {
            incorrectPasswordText.setText("This field cannot be left empty");
            incorrectPasswordText.setVisible(true);
        }
    }
}
