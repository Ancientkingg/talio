/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import com.google.inject.Inject;

import client.utils.ServerUtils;
import commons.Person;
import commons.Quote;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;

public class AddQuoteCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField quote;

    /**
     * Constructs a scene controller that handles adding quotes
     * @param server The server interface
     * @param mainCtrl The main scene controller
     */
    @Inject
    public AddQuoteCtrl(final ServerUtils server, final MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;

    }

    /**
     * Runs when the user presses the cancel button
     */
    public void cancel() {
        clearFields();
        mainCtrl.showOverview();
    }

    /**
     * Runs when the user presses the OK button
     */
    public void ok() {
        try {
            server.addQuote(getQuote());
        } catch (WebApplicationException e) {

            final Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        clearFields();
        mainCtrl.showOverview();
    }

    /**
     * Gets the quote
     * @return a copy of the quote of this controller
     */
    private Quote getQuote() {
        final Person p = new Person(firstName.getText(), lastName.getText());
        final String q = quote.getText();
        return new Quote(p, q);
    }

    /**
     * Clears the fields in the window
     */
    private void clearFields() {
        firstName.clear();
        lastName.clear();
        quote.clear();
    }

    /**
     * Controls what to do when certain keys are pressed
     * @param e the keyEvent that is triggered
     */
    public void keyPressed(final KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                ok();
                break;
            case ESCAPE:
                cancel();
                break;
            default:
                break;
        }
    }
}