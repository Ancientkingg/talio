package client.scenes.components;

import client.Main;
import commons.Card;
import commons.SubTask;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class SubTaskComponent extends GridPane implements UIComponent {

    private final Card card;

    private final SubTask subTask;

    private final String description;

    @FXML
    private CheckBox checkBox;

    @FXML
    private TextField descriptionField;


    public SubTaskComponent(final Card card, final SubTask subTask, final String description) {
        this.card = card;
        this.subTask = subTask;
        this.description = description;

        loadSource(Main.class.getResource("/components/SubTask.fxml"));

        try {
            descriptionField.setText(subTask.getDescription());

            if (subTask.isDone()) {
                checkBox.selectedProperty();
            };

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    


}
