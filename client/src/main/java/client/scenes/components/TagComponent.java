package client.scenes.components;

import client.Main;
import client.scenes.OverviewCtrl;
import client.scenes.Refreshable;
import client.scenes.components.modals.TagSettingsModal;
import client.services.BoardService;
import commons.Color;
import commons.ColorScheme;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.util.Objects;

public class TagComponent extends GridPane implements UIComponent {

    private final BoardService boardService;

    private final Tag tag;

    private final Scene parentScene;

    private final Refreshable parentCtrl;

    @FXML
    private Text tagTitle;

    @FXML
    private Pane colorBubble;

    @FXML
    private Button editButton;


    /**
     * Constructor for tag
     *
     * @param boardService boardService instance
     * @param parentScene  parent scene
     * @param parentCtrl parent controller
     * @param tag tag to be displayed
     */
    public TagComponent(final BoardService boardService, final Scene parentScene, final Refreshable parentCtrl, final Tag tag) {
        this.boardService = boardService;
        this.parentScene = parentScene;
        this.parentCtrl = parentCtrl;
        this.tag = tag;

        loadSource(Main.class.getResource("/components/Tag.fxml"));

        try {
            tagTitle.textProperty().set(tag.getTitle());

            final ColorScheme colorScheme = tag.getColorScheme();
            final Color backgroundColor = colorScheme.getBackgroundColor();

            final String primaryStyle;


            if (backgroundColor == null || Objects.equals(backgroundColor, new Color(0, 0, 0, 0))) {
                primaryStyle = "-fx-background-color: " + this.colorGenerator() + ";";
            } else {
                primaryStyle = "-fx-background-color: " + backgroundColor + ";";
            }

            final String secondaryStyle;

            final commons.Color textColor = colorScheme.getTextColor();
            if (textColor == null || Objects.equals(textColor, new Color(0,0,0,0))) {
                secondaryStyle = "-fx-border-color: " + this.colorGenerator() + ";";
            } else {
                secondaryStyle = "-fx-border-color: " + textColor + ";";
            }

            colorBubble.setStyle(primaryStyle + secondaryStyle);
        } catch (NullPointerException e) {
            throw new RuntimeException(e);
        }

        this.refreshLock();
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
        final TagSettingsModal tagSettingsModal = new TagSettingsModal(boardService, parentScene, parentCtrl, tag);
        tagSettingsModal.showModal();
    }

    private void refreshLock() {
        if (OverviewCtrl.isLocked()) {
            editButton.setVisible(false);
            editButton.setDisable(true);
        } else {
            editButton.setVisible(true);
            editButton.setDisable(false);
        }
    }
}
