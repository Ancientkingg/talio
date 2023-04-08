package client.scenes.components;

import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.net.URL;

public interface UIComponent {

    /**
     * Loads the FXML source for the component
     * @param location URL
     */
    default void loadSource(URL location) {
        final FXMLLoader loader = new FXMLLoader(location);
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to load the following FXML file: " + location.getPath());
        }
    }
}
