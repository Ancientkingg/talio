package client.scenes;

import client.exceptions.BoardChangeException;
import commons.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import javax.inject.Inject;
import java.util.TreeSet;

public class CreateCardCtrl {

    private final MainCtrl mainCtrl;

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
     */
    @Inject
    public CreateCardCtrl(final MainCtrl mainCtrl) {
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
        mainCtrl.addCard(card, (Column) columnMenu.getValue());
    }

    /**
     *
     */
    public void loadMenuItems() {
        columnMenu.setConverter(stringConverter);
        for (final Column col : mainCtrl.getCurrentBoard().getColumns()) {
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
