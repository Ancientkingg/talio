package client.scenes.components.modals;

import client.Main;
import client.scenes.LiveUIController;
import client.scenes.components.ColorPresetComponent;
import client.scenes.components.UIComponent;
import client.services.BoardService;
import commons.Board;
import commons.Color;
import commons.ColorScheme;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

import java.util.List;

public class ColorPresetsOverviewModal extends Modal implements UIComponent, LiveUIController {


    @FXML
    private VBox colorPresetsContainer;

    /**
     * Constructor for BoardSettingsModal
     * @param boardService boardService instance
     * @param parentScene parent scene (displayed under modal)
     */
    public ColorPresetsOverviewModal(final BoardService boardService, final Scene parentScene) {
        super(boardService, parentScene);

        loadSource(Main.class.getResource("/components/ColorPresetsOverviewModal.fxml"));
    }

    /**
     * Initialize modal
     */
    @FXML
    public void initialize() {
        super.initialize();
        this.renderColorPresets();
    }

    @FXML
    private void onAddColorPresetButtonClick() {
        final ColorScheme colorPreset = new ColorScheme("New Color Preset", new Color(colorGenerator()), new Color(colorGenerator()));
        boardService.addColorPresetToCurrentBoard(colorPreset);

        refresh();
    }

    /**
     * Refreshes the modal
     */
    public void refresh() {
        this.renderColorPresets();
    }

    private void renderColorPresets() {
        colorPresetsContainer.getChildren().clear();

        final Board currentBoard = boardService.getCurrentBoard();
        final List<ColorScheme> colorPresets = currentBoard.getColorPresets();

        for (final ColorScheme colorPreset : colorPresets) {
            final ColorPresetComponent colorPresetComponent = new ColorPresetComponent(boardService, parentScene, this, colorPreset);
            colorPresetsContainer.getChildren().add(colorPresetComponent);
        }
    }

    /**
     * Returns a random color for a tag
     * @return String containing hex code
     */
    private String colorGenerator() {
        final String [] colors = {"#2196F3", "#92D36E", "#FF3823", "#92D36E",
            "#00FFFF", "#FF0000", "#ff9a00", "#694130", "#5A5A82" };

        return colors [(int) (Math.random() * colors.length)];
    }
}
