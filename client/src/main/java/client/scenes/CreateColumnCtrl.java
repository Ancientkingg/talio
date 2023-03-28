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
        final Column column = new Column(columnName.getText().isEmpty() ?
                columnName.getPromptText() : columnName.getText(), demoIndexCounter++, new TreeSet<>());
        boardService.addColumnToCurrentBoard(column);
        mainCtrl.showOverview();
        mainCtrl.refreshOverview();
        columnName.setPromptText(getFunColumnName());
    }

    /**
     * Clears fields to avoid accidental repetition of prior arguments
     */
    public void clearFields() {
        columnName.clear();
    }

    /**
     * Generates a random column name
     * @return a fun column name
     */
    private String getFunColumnName() {
        final String[] adjectives = { "fun", "cool", "awesome", "great", "amazing", "wonderful", "fantastic",
                                        "incredible", "magnificent", "marvelous", "spectacular", "superb",
                                        "super", "fabulous", "fab", "excellent", "excellent", "terrific", "terrific",
                                        "wicked", "wicked"};

        final String[] animalNames = {"dog", "cat", "fish", "bird", "hamster", "rabbit", "turtle", "snake", "lizard",
                                      "frog", "toad", "salamander", "chameleon", "gecko", "iguana", "alligator",
                                      "crocodile", "lizard", "snake", "turtle", "tortoise", "shark", "whale",
                                      "dolphin", "seal", "otter", "bear", "panda", "monkey", "gorilla", "ape",
                                      "chimpanzee", "orangutan", "gibbon", "lemur", "squirrel", "chipmunk",
                                      "mouse", "rat", "rabbit", "hare", "deer", "elk", "moose", "buffalo", "cow",
                                      "bull", "horse", "pony", "zebra", "giraffe", "rhinoceros", "hippopotamus",
                                      "elephant", "camel", "llama", "alpaca", "sheep", "goat", "pig", "boar",
                                      "wolf", "fox", "coyote", "dog", "cat", "lion", "tiger", "leopard", "cheetah",
                                      "jaguar", "panther", "hyena", "bear", "panda", "koala", "kangaroo", "wallaby",
                                      "wombat", "opossum", "platypus", "kookaburra", "emu"};

        return adjectives[(int) (Math.random() * adjectives.length)]
                + " " + animalNames[(int) (Math.random() * animalNames.length)];
    }
}
