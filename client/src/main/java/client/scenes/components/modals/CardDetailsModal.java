package client.scenes.components.modals;

import client.Main;
import client.scenes.components.CardComponent;
import client.services.BoardService;
import commons.Card;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class CardDetailsModal extends Modal {

    private final Card card;

    private final CardComponent cardComponent;

    @FXML
    private TextArea cardTitle;

    @FXML
    private TextArea cardDescription;

    @FXML
    private VBox tagBox;

    /**
     * Constructor for card details modal
     * @param boardService shared boardService instance
     * @param parentScene the scene over which the modal has to overlay
     * @param card the card from which the modal is called
     * @param cardComponent parent cardComponent
     */
    public CardDetailsModal(final BoardService boardService, final Scene parentScene, final Card card, final CardComponent cardComponent) {
        super(boardService, parentScene);
        this.card = card;
        this.cardComponent = cardComponent;

        final FXMLLoader loader = new FXMLLoader(Main.class.getResource("/components/CardDetailsModal.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        refreshTags();

    }

    /**
     * Initialize Modal
     */
    @FXML
    public void initialize() {
        super.initialize();
        this.cardTitle.setText(card.getTitle());
        this.cardDescription.setText(card.getDescription());
    }

    /**
     * Submits data and refreshes overview when submit button is pressed;
     */
    @FXML
    private void submitDetails() {
        this.card.setTitle(cardTitle.getText());
        this.card.setDescription(cardDescription.getText());
        this.closeModal();
        this.cardComponent.refresh();
    }

    @FXML
    private void deleteCard() {
        this.cardComponent.getColumnParent().deleteCard(this.cardComponent);
        this.cardComponent.getColumnParent().getColumn().removeCard(this.card);
        this.closeModal();
        this.cardComponent.getColumnParent().refresh();
    }


    /**
     * Refresh description textArea
     */
    public void refreshDescription() {
        cardDescription.setText(card.getDescription());
    }

    /**
     * Refresh title textArea
     */
    public void refreshTitle() { cardTitle.setText(card.getTitle()); }

    /**
     * Refreshes tags in the scroll pane and displays them in their component form
     */
    public void refreshTags() {
        // TODO
    }

}
