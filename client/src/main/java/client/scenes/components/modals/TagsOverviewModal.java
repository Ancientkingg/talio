package client.scenes.components.modals;

import client.Main;
import client.scenes.OverviewCtrl;
import client.scenes.Refreshable;
import client.scenes.components.TagComponent;
import client.scenes.components.UIComponent;
import client.services.BoardService;
import commons.Board;
import commons.Color;
import commons.ColorScheme;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.Set;

public class TagsOverviewModal extends Modal implements UIComponent, Refreshable {


    @FXML
    private VBox tagsContainer;

    @FXML
    private Button addTagButton;

    /**
     * Constructor for BoardSettingsModal
     * @param boardService boardService instance
     * @param parentScene parent scene (displayed under modal)
     */
    public TagsOverviewModal(final BoardService boardService, final Scene parentScene) {
        super(boardService, parentScene);

        loadSource(Main.class.getResource("/components/TagsOverviewModal.fxml"));

        this.refreshLock();
    }

    /**
     * Initialize modal
     */
    @FXML
    public void initialize() {
        super.initialize();
        this.renderTags();
    }

    @FXML
    private void onAddTagButtonClick() {
        final Tag tag = new Tag("New Tag", new ColorScheme(new Color(colorGenerator()), new Color(colorGenerator())));
        boardService.addTagToCurrentBoard(tag);

        refresh();
    }

    /**
     * Refreshes the modal
     */
    public void refresh() {
        this.renderTags();
        this.refreshLock();
    }

    private void refreshLock() {
        if (OverviewCtrl.isLocked()) {
            addTagButton.setDisable(true);
            addTagButton.setVisible(false);
        } else {
            addTagButton.setDisable(false);
            addTagButton.setVisible(true);
        }
    }

    protected void renderTags() {
        tagsContainer.getChildren().clear();

        final Board currentBoard = boardService.getCurrentBoard();
        final Set<Tag> tags = currentBoard.getTags();

        for (final Tag tag : tags) {
            final TagComponent tagComponent = new TagComponent(boardService, parentScene, this, tag);
            tagsContainer.getChildren().add(tagComponent);
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
