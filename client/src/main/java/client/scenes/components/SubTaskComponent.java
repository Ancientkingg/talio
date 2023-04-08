package client.scenes.components;

import client.Main;
import commons.Card;
import commons.SubTask;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;

public class SubTaskComponent extends GridPane implements UIComponent {

    private final SubTask subTask;

    @FXML
    private CheckBox checkBox;

    @FXML
    private TextField descriptionField;

    @FXML
    private Button editDescriptionButton;

    /**
     * Loads new subtask component
     * @param subTask subTask object
     */
    public SubTaskComponent(final SubTask subTask) {
        this.subTask = subTask;

        loadSource(Main.class.getResource("/components/SubTask.fxml"));

        try {
            descriptionField.setText(subTask.getDescription());

            if (subTask.isDone()) {
                checkBox.selectedProperty();
            };

        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        descriptionField.setOnKeyReleased(e -> { // Disable editing of description when enter is pressed
            if (e.getCode() == KeyCode.ENTER) {
                descriptionField.setDisable(true);
            }
        });

        editDescriptionButton.setOnAction(event -> {
            descriptionField.setDisable(false);
            descriptionField.requestFocus();
        });

        descriptionField.setDisable(true); //Disables editing by default
        this.listenForChanges();
    }

    /**
     * Listens for changes in subtask content
     */
    public void listenForChanges () {
        descriptionField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                subTask.setDescription(descriptionField.getText());
                descriptionField.setDisable(true);
            }
        });

        descriptionField.textProperty().addListener((observable, oldValue, newValue) -> {
            subTask.setDescription(descriptionField.getText());
        });

        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                subTask.setDone(true);
            }
        });
    }




}
