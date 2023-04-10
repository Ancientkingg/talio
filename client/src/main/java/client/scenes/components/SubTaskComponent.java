package client.scenes.components;

import client.Main;
import client.scenes.components.modals.CardDetailsModal;
import client.services.BoardService;
import commons.Card;
import commons.SubTask;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import lombok.Getter;

import java.io.IOException;
import java.util.function.Consumer;

public class SubTaskComponent extends Draggable implements UIComponent {

    @Getter
    private final SubTask subTask;

    private final BoardService boardService;

    private final Card card;

    private final Consumer<Void> refresh;

    private final CardDetailsModal parentModal;

    @FXML
    private CheckBox checkBox;

    @FXML
    private TextField descriptionField;

    @FXML
    private Button editDescriptionButton;

    /**
     * Loads new subtask component
     *
     * @param subTask          subTask object
     * @param boardService     shared boardService instance
     * @param card             parent card
     * @param refresh          refresh callback
     * @param cardDetailsModal parent card details modal
     */
    public SubTaskComponent(final SubTask subTask, final Card card, final BoardService boardService,
                            final Consumer<Void> refresh, final CardDetailsModal cardDetailsModal)
    {
        super(cardDetailsModal, cardDetailsModal.getSubTasksContainer());
        this.subTask = subTask;
        this.boardService = boardService;
        this.card = card;
        this.refresh = refresh;
        this.parentModal = cardDetailsModal;

        final FXMLLoader loader = new FXMLLoader(Main.class.getResource("/components/SubTask.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        setDraggable(true);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            descriptionField.setText(subTask.getDescription());

            if (subTask.isDone()) {
                checkBox.selectedProperty();
            }
            ;

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        descriptionField.setOnKeyReleased(e -> { // Disable editing of description when enter is pressed
            if (e.getCode() == KeyCode.ENTER) {
                descriptionField.setDisable(true);
                subTask.setDescription(descriptionField.getText());
                boardService.editSubTask(card, subTask);
            }
        });

        editDescriptionButton.setOnAction(event -> {
            descriptionField.setDisable(false);
            descriptionField.requestFocus();
        });

        descriptionField.setDisable(true); //Disables editing by default
        checkBox.setSelected(subTask.isDone());
        this.listenForChanges();
    }

    /**
     * Listens for changes in subtask content
     */
    public void listenForChanges() {
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
            subTask.setDone(newValue);
            boardService.toggleSubTask(card, subTask);
            refresh.accept(null);
            System.out.println("Test");
        });
    }

    /**
     * Deletes subtask from card
     */
    public void onDelete() {
        this.boardService.removeSubTask(this.card, this.subTask);
        this.refresh.accept(null);
    }

    @Override
    protected void onDrop(final Node intersectedComponent, final boolean isBelow) {
        if (!(intersectedComponent instanceof SubTaskComponent)) {
            return;
        }
        boardService.moveSubTask(card, subTask,
                ((SubTaskComponent) intersectedComponent).getSubTask().getPriority());
        refresh.accept(null);
    }

    @Override
    protected void duringDrag(final Node intersectedComponent, final boolean isBelow) {

    }

    /**
     * Clones subtask component
     * @return cloned subtask component
     */
    @Override
    public Draggable clone() {
        return new SubTaskComponent(subTask, card, boardService, refresh, this.parentModal);
    }
}
