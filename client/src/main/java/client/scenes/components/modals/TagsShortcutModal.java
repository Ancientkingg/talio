package client.scenes.components.modals;

import client.Main;
import client.scenes.Refreshable;
import client.scenes.components.CardComponent;
import client.scenes.components.TagSelectComponent;
import client.scenes.components.UIComponent;
import client.services.BoardService;
import commons.Board;
import commons.Card;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class TagsShortcutModal extends Modal implements UIComponent, Refreshable {

    @FXML
    private VBox tagsContainer;

    private final Refreshable parentCtrl;

    private final CardComponent cardComponent;

    /**
     * Constructor for TagsShortcutModal
     * @param boardService dependency injection for BoardService
     * @param parentScene dependency injection for Parent Scene
     * @param parentCtrl dependency injection for Parent Ctrl
     * @param cardComponent Card Component whose tags are being changed
     */
    public TagsShortcutModal(final BoardService boardService, final Scene parentScene, final Refreshable parentCtrl, final CardComponent cardComponent) {
        super(boardService, parentScene);
        this.parentCtrl = parentCtrl;
        this.cardComponent = cardComponent;

        loadSource(Main.class.getResource("/components/TagsShortcutModal.fxml"));
    }

    /**
     * setup Modal
     */
    @Override
    public void initialize() {
        super.initialize();
        renderTags();
    }

    protected void renderTags() {
        tagsContainer.getChildren().clear();

        final Board currentBoard = boardService.getCurrentBoard();
        final Set<Tag> tags = currentBoard.getTags();

        for (final Tag tag : tags) {
            final TagSelectComponent tagSelectComponent = new TagSelectComponent(boardService, parentScene, parentCtrl, tag);
            if (cardComponent.getCard().getTags().contains(tag)) {
                tagSelectComponent.setSelected(true);
            }
            tagsContainer.getChildren().add(tagSelectComponent);
        }
    }

    @FXML
    private void submitDetails () {
        final List<Tag> selectedTags = this.getSelectedTags();

        final Card card = cardComponent.getCard();

        for (final Tag tag : selectedTags) {
            if (!card.getTags().contains(tag)) {
                card.addTag(tag);
                boardService.addTagToCard(card, tag);
            }
        }

        final Iterator<Tag> it = card.getTags().iterator();
        while (it.hasNext()) {
            final Tag tag = it.next();
            if (!selectedTags.contains(tag)) {
                it.remove();
                boardService.removeTagFromCard(card, tag);
            }
        }

        boardService.editCard(card, cardComponent.getColumnParent().getColumn());

        cardComponent.refresh();
        this.closeModal();
    }

    private List<Tag> getSelectedTags() {
        return this.tagsContainer.getChildren().stream()
                .filter( child -> child instanceof TagSelectComponent)
                .map( child -> (TagSelectComponent) child)
                .filter(TagSelectComponent::isSelected)
                .map(TagSelectComponent::getTag).toList();
    }

    /**
     *  refresh the modal
     */
    @Override
    public void refresh() {
        renderTags();
    }
}
