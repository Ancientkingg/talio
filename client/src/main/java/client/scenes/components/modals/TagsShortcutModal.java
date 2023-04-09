package client.scenes.components.modals;

import client.Main;
import client.scenes.Refreshable;
import client.scenes.components.TagComponent;
import client.scenes.components.TagSelectComponent;
import client.scenes.components.UIComponent;
import client.services.BoardService;
import commons.Board;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.Set;

public class TagsShortcutModal extends TagsOverviewModal implements UIComponent {

    @FXML
    private VBox tagsContainer;

    @FXML
    private Button addTagButton;

    final Refreshable parentCtrl;

    /**
     * Constructor for TagsShortcutModal
     * @param boardService dependency injection for BoardService
     * @param parentScene dependency injection for Parent Scene
     * @param parentCtrl dependency injection for Parent Ctrl
     */
    public TagsShortcutModal(final BoardService boardService, final Scene parentScene, final Refreshable parentCtrl) {
        super(boardService, parentScene);
        this.parentCtrl = parentCtrl;

        loadSource(Main.class.getResource("/components/TagsOverviewModal.fxml"));
    }

    /**
     * setup Modal
     */
    @Override
    public void initialize() {
        super.initialize();
        addTagButton.setDisable(true);
        addTagButton.setVisible(false);
    }

    @Override
    protected void renderTags() {
        tagsContainer.getChildren().clear();

        final Board currentBoard = boardService.getCurrentBoard();
        final Set<Tag> tags = currentBoard.getTags();

        for (final Tag tag : tags) {
            final TagSelectComponent tagSelectComponent = new TagSelectComponent(boardService, parentScene, parentCtrl, tag);
            tagsContainer.getChildren().add(tagSelectComponent);
        }
    }
}
