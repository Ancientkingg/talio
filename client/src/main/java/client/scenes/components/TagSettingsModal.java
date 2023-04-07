package client.scenes.components;

import client.Main;
import client.scenes.LiveUIController;
import client.services.BoardService;
import commons.ColorScheme;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class TagSettingsModal extends Modal implements UIComponent {

    @FXML
    private TextField titleTextField;

    @FXML
    private ColorPicker primaryColor;

    @FXML
    private ColorPicker secondaryColor;

    private final LiveUIController parentCtrl;

    private final Tag tag;

    /**
     * Constructor for BoardSettingsModal
     * @param boardService boardService instance
     * @param parentScene parent scene (displayed under modal)
     * @param parentCtrl parent controller
     * @param tag tag to be displayed
     */
    public TagSettingsModal(final BoardService boardService, final Scene parentScene, final LiveUIController parentCtrl, final Tag tag) {
        super(boardService, parentScene);
        this.parentCtrl = parentCtrl;
        this.tag = tag;

        loadSource(Main.class.getResource("/components/TagSettingsModal.fxml"));

        this.titleTextField.setText(tag.getTitle());

        final ColorScheme colorScheme = tag.getColorScheme();

        final commons.Color backgroundColor = colorScheme.getBackgroundColor();
        final commons.Color textColor = colorScheme.getTextColor();

        final Color primaryColor = Color.rgb(backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue(),
                backgroundColor.getAlpha() / 255.0);
        final Color secondaryColor = Color.rgb(textColor.getRed(), textColor.getGreen(), textColor.getBlue(), textColor.getAlpha() / 255.0);

        this.primaryColor.setValue(primaryColor);
        this.secondaryColor.setValue(secondaryColor);
    }

    /**
     * Initialize modal
     */
    @FXML
    public void initialize() {
        super.initialize();
    }


    @FXML
    private void submitModal() {
        final String title = this.titleTextField.getText();
        final Color primaryColor = this.primaryColor.getValue();
        final Color secondaryColor = this.secondaryColor.getValue();

        final ColorScheme colorScheme = new ColorScheme(
            new commons.Color((int) (secondaryColor.getRed() * 255), (int) (secondaryColor.getGreen() * 255), (int) (secondaryColor.getBlue() * 255),
                    (int) (secondaryColor.getOpacity() * 255)),
            new commons.Color((int) (primaryColor.getRed() * 255), (int) (primaryColor.getGreen() * 255), (int) (primaryColor.getBlue() * 255),
                    (int) (primaryColor.getOpacity() * 255))
        );

        this.tag.setTitle(title);
        this.tag.setColorScheme(colorScheme);

        boardService.editTag(tag);
        this.closeModal();
    }

    @FXML
    private void onDelete() {
        boardService.removeTagFromBoard(tag);
        parentCtrl.refresh();
        this.closeModal();
    }
}
