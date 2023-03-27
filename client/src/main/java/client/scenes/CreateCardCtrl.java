package client.scenes;

import client.exceptions.BoardChangeException;
import client.services.BoardService;
import commons.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import javax.inject.Inject;
import java.util.TreeSet;

public class CreateCardCtrl {

    private final MainCtrl mainCtrl;
    private final BoardService boardService;

    @FXML
    private ChoiceBox columnMenu;
    @FXML
    private TextField cardTitle;
    @FXML
    private TextArea cardDescription;
    @FXML
    private TextField cardPriority;

    final private StringConverter<Column> stringConverter;


    /**
     * Injects mainCtrl instance into controller to allow access to its methods
     * @param mainCtrl Shared instance of MainCtrl
     * @param boardService Shared instance of BoardModel
     */
    @Inject
    public CreateCardCtrl(final MainCtrl mainCtrl, final BoardService boardService) {
        this.boardService = boardService;
        this.mainCtrl = mainCtrl;

        stringConverter = new StringConverter<Column>() {
            @Override
            public String toString(final Column object) {
                return object.getHeading();
            }

            @Override
            public Column fromString(final String string) {
                return null;
            }
        };
    }

    /**
     * Routes the user to the overview of the card they just created
     */
    public void createCard() throws BoardChangeException {
        final Card card = new Card(cardTitle.getText(), Integer.parseInt(cardPriority.getText()), cardDescription.getText(), new TreeSet<Tag>());
        boardService.addCardToColumn(card, (Column) columnMenu.getValue());
    }

    /**
     * Loads the menu items for the column menu
     */
    public void loadMenuItems() {
        columnMenu.setConverter(stringConverter);
        for (final Column col : boardService.getCurrentBoard().getColumns()) {
            columnMenu.getItems().add(col);
        }
    }

    /**
     * Clears fields to avoid accidental repetition of prior arguments
     */
    public void clearFields() {
        columnMenu.getItems().clear();
        cardTitle.clear();
        cardDescription.clear();
        cardPriority.clear();
    }
}
