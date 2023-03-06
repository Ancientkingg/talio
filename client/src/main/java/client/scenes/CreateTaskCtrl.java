package client.scenes;

import client.exceptions.BoardChangeException;
import commons.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import javax.inject.Inject;
import java.util.TreeSet;

public class CreateTaskCtrl {

    private final MainCtrl mainCtrl;

    @FXML
    private ChoiceBox columnMenu;
    @FXML
    private TextField taskTitle;
    @FXML
    private TextArea taskDescription;
    @FXML
    private TextField taskPriority;

    final private StringConverter<Column> stringConverter;


    /**
     * Injects mainCtrl instance into controller to allow access to its methods
     * @param mainCtrl Shared instance of MainCtrl
     */
    @Inject
    public CreateTaskCtrl(final MainCtrl mainCtrl) {
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
     * Routes the user to the overview of the task they just created
     */
    public void createTask() throws BoardChangeException {
        final Card task = new Card(taskTitle.getText(), Integer.parseInt(taskPriority.getText()), taskDescription.getText(), new TreeSet<Tag>());
        mainCtrl.addTask(task, (Column) columnMenu.getValue());
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
        taskTitle.clear();
        taskDescription.clear();
        taskPriority.clear();
    }
}
