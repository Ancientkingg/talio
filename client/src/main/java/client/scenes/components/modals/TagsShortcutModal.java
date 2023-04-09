package client.scenes.components.modals;

import client.Main;
import client.scenes.components.TagComponent;
import client.scenes.components.UIComponent;
import client.services.BoardService;
import commons.Board;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

import java.util.Set;

public class TagsShortcutModal extends TagsOverviewModal implements UIComponent {

    @FXML
    private VBox tagsContainer;

    /**
     * Constructor for TagsShortcutModal
     * @param boardService dependency injection for BoardService
     * @param parentScene dependency injection for Parent Scene
     */
    public TagsShortcutModal(final BoardService boardService, final Scene parentScene) {
        super(boardService, parentScene);

        loadSource(Main.class.getResource("/components/TagsOverviewModal.fxml"));
    }

    /**
     *
     */
    @Override
    public void initialize() {
        super.initialize();

    }

    @Override
    protected void renderTags() {
        tagsContainer.getChildren().clear();

        final Board currentBoard = boardService.getCurrentBoard();
        final Set<Tag> tags = currentBoard.getTags();

        for (final Tag tag : tags) {
            final TagComponent tagComponent = new TagComponent(boardService, parentScene, this, tag);
            tagsContainer.getChildren().add(tagComponent);
        }
    }
}
