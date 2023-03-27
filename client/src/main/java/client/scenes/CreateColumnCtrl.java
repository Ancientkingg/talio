package client.scenes;

import client.exceptions.BoardChangeException;
import client.services.BoardService;
import commons.*;

import java.util.TreeSet;

import javafx.fxml.FXML;
import javax.inject.Inject;

import javafx.scene.control.TextField;


public class CreateColumnCtrl {

    private final MainCtrl mainCtrl;
    private final BoardService boardService;

    @FXML
    private TextField columnName;
    private static int demoIndexCounter; //This is just a temporary fix to give columns different indexes


    /**
     * Injects mainCtrl instance into controller to allow access to its methods
     * @param mainCtrl Shared instance of MainCtrl
     * @param boardService Shared instance of BoardService
     */
    @Inject
    public CreateColumnCtrl(final MainCtrl mainCtrl, final BoardService boardService) {
        demoIndexCounter = 1;
        this.mainCtrl = mainCtrl;
        this.boardService = boardService;
    }

    /**
     * Will be used to create a column when user passes through the column name
     */
    public void createColumn() throws BoardChangeException {
        final Column column = new Column(columnName.getText(), demoIndexCounter++, new TreeSet<>());
        boardService.addColumnToCurrentBoard(column);
        mainCtrl.showOverview();
        mainCtrl.refreshOverview();
    }

    /**
     * Clears fields to avoid accidental repetition of prior arguments
     */
    public void clearFields() {
        columnName.clear();
    }
}
