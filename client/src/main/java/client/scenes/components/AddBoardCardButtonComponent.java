package client.scenes.components;

import client.Main;
import client.scenes.HomePageCtrl;
import client.scenes.components.modals.CreateBoardModal;
import client.services.BoardService;
import javafx.scene.layout.Pane;

public class AddBoardCardButtonComponent extends Pane implements UIComponent {

    private final BoardService boardService;
    private final HomePageCtrl parentCtrl;

    /**
     * Constructor for AddBoardCardButtonComponent
     * @param boardService BoardService instance
     * @param parentCtrl HomePageCtrl instance
     */
    public AddBoardCardButtonComponent(final BoardService boardService, final HomePageCtrl parentCtrl) {
        this.boardService = boardService;
        this.parentCtrl = parentCtrl;

        loadSource(Main.class.getResource("/components/AddBoardCardButton.fxml"));

        setHover();
    }

    /**
     * Sets the hover effect for the component
     */
    private void setHover() {
        this.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                this.getStyleClass().add("hovered");
            } else {
                this.getStyleClass().remove("hovered");
            }
        });
    }


    /**
     * Click handler for the component
     */
    public void onClick() {
        final CreateBoardModal modal = new CreateBoardModal(boardService, this.getScene(), parentCtrl);
        modal.showModal();
    }
}
