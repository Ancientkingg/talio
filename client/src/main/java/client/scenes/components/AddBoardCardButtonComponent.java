package client.scenes.components;

import client.Main;
import client.scenes.HomePageCtrl;
import client.services.BoardService;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class AddBoardCardButtonComponent extends Pane {

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

        final FXMLLoader loader = new FXMLLoader(Main.class.getResource("/components/AddBoardCardButton.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
