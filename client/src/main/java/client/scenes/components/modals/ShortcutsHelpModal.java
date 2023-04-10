package client.scenes.components.modals;

import client.Main;
import client.scenes.components.UIComponent;
import client.services.BoardService;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class ShortcutsHelpModal extends Modal implements UIComponent {

    @FXML
    private Button okButton;
    /**
     * Default constructor for modal
     *
     * @param boardService boardService instance
     * @param parentScene  scene behind modal
     */
    public ShortcutsHelpModal(final BoardService boardService, final Scene parentScene) {
        super(boardService, parentScene);

        loadSource(Main.class.getResource("/components/ShortcutsHelpModal.fxml"));

        okButton.requestFocus();
    }

    @FXML
    private void okButton() {
        closeModal();
    }
}
