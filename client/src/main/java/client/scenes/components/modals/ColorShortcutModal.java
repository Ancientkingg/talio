package client.scenes.components.modals;

import client.Main;
import client.scenes.OverviewCtrl;
import client.scenes.Refreshable;
import client.scenes.components.CardComponent;
import client.scenes.components.UIComponent;
import client.services.BoardService;
import commons.ColorScheme;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.util.List;

public class ColorShortcutModal extends Modal implements Refreshable, UIComponent {

    @FXML
    private ComboBox<ColorScheme> colorSchemeComboBox;

    @FXML
    private Button submitButton;

    private final Refreshable parentCtrl;

    private final CardComponent cardComponent;

    /**
     * Constructor for ColorShortcutModal
     *
     * @param boardService dependency injection for BoardService
     * @param parentScene dependency injection for Parent Scene
     * @param parentCtrl dependency injection for Parent Ctrl
     * @param cardComponent Card Component whose tags are being changed
     */
    public ColorShortcutModal(final BoardService boardService, final Scene parentScene, final Refreshable parentCtrl, final CardComponent cardComponent) {
        super(boardService, parentScene);
        this.parentCtrl = parentCtrl;
        this.cardComponent = cardComponent;

        loadSource(Main.class.getResource("/components/ColorShortcutModal.fxml"));

        this.refreshLock();
    }

    /**
     * initialize the modal
     */
    @Override
    public void initialize() {
        super.initialize();
        refresh();
    }

    /**
     * Refresh the modal
     */
    @Override
    public void refresh() {
        this.colorSchemeComboBox.getItems().clear();
        final List<ColorScheme> colorSchemes = boardService.getCurrentBoard().getColorPresets();
        this.colorSchemeComboBox.getItems().addAll(colorSchemes);

        this.refreshLock();
    }

    private void refreshLock() {
        if (OverviewCtrl.isLocked()) {
            submitButton.setDisable(true);
            submitButton.setVisible(false);

            colorSchemeComboBox.setDisable(true);
        } else {
            submitButton.setDisable(false);
            submitButton.setVisible(true);

            colorSchemeComboBox.setDisable(false);
        }
    }

    @FXML
    private void submitDetails() {
        cardComponent.getCard().setColorScheme(this.colorSchemeComboBox.getValue());

        boardService.editCard(cardComponent.getCard(), cardComponent.getColumnParent().getColumn());

        cardComponent.refresh();
        this.closeModal();
    }
}
