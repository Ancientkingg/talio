package client.scenes.components.modals;

import client.Main;
import client.scenes.components.UIComponent;
import client.services.BoardService;
import javafx.scene.Scene;

public class ShortcutsHelpModal extends Modal implements UIComponent {

    /**
     * Default constructor for modal
     *
     * @param boardService boardService instance
     * @param parentScene  scene behind modal
     */
    public ShortcutsHelpModal(final BoardService boardService, final Scene parentScene) {
        super(boardService, parentScene);

        loadSource(Main.class.getResource("/components/modals/ShortcutsHelpModal.fxml"));
    }


}
