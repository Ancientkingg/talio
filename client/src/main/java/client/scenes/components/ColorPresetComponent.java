package client.scenes.components;

import client.Main;
import client.scenes.LiveUIController;
import client.scenes.components.modals.ColorPresetSettingsModal;
import client.services.BoardService;
import commons.ColorScheme;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.awt.*;
import java.util.Objects;

public class ColorPresetComponent extends GridPane implements UIComponent {

    private final BoardService boardService;

    private final ColorScheme colorPreset;

    private final Scene parentScene;

    private final LiveUIController parentCtrl;

    @FXML
    private Text tagTitle;

    @FXML
    private Pane colorBubble;


    /**
     * Constructor for tag
     *
     * @param boardService boardService instance
     * @param parentScene  parent scene
     * @param parentCtrl parent controller
     * @param colorPreset color preset
     */
    public ColorPresetComponent(final BoardService boardService, final Scene parentScene, final LiveUIController parentCtrl, final ColorScheme colorPreset) {
        this.boardService = boardService;
        this.parentScene = parentScene;
        this.parentCtrl = parentCtrl;
        this.colorPreset = colorPreset;

        loadSource(Main.class.getResource("/components/ColorPreset.fxml"));

        try {
            tagTitle.textProperty().set(colorPreset.getName());

            final commons.Color backgroundColor = colorPreset.getBackgroundColor();

            final String primaryStyle;


            if (backgroundColor == null || Objects.equals(backgroundColor, new Color(0, 0, 0, 0))) {
                primaryStyle = "-fx-background-color: " + this.colorGenerator() + ";";
            } else {
                primaryStyle = "-fx-background-color: " + backgroundColor + ";";
            }

            final String secondaryStyle;

            final commons.Color textColor = colorPreset.getTextColor();
            if (textColor == null || Objects.equals(textColor, new Color(0,0,0,0))) {
                secondaryStyle = "-fx-border-color: " + this.colorGenerator() + ";";
            } else {
                secondaryStyle = "-fx-border-color: " + textColor + ";";
            }

            colorBubble.setStyle(primaryStyle + secondaryStyle);
        } catch (NullPointerException e) {
            throw new RuntimeException(e);
        }


    }

    /**
     * Returns a random color for a tag
     * @return String containing hex code
     */
    String colorGenerator() {
        final String [] colors = {"#2196F3", "#92D36E", "#FF3823", "#92D36E",
                                  "#00FFFF", "#FF0000", "#ff9a00", "#694130", "#5A5A82" };

        return colors [(int) (Math.random() * colors.length)];
    }

    @FXML
    private void onEdit() {
        final ColorPresetSettingsModal colorPresetSettingsModal = new ColorPresetSettingsModal(boardService, parentScene, parentCtrl, colorPreset);
        colorPresetSettingsModal.showModal();
    }
}
