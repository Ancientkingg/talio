package client.items;

import java.util.List;

public class Column {

    private String title;
    private List taskList;

    /**
     * Constructs column
     * @param title String title to be displayed
     */
    public Column(final String title) {
        this.title = title;
    }

    public String getTitle() { return title; }
}
