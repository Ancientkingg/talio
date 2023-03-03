package client.items;

import java.util.LinkedList;
import java.util.List;

public class Board {

    private String title;
    private String password;
    private List<Column> columnList;

    /**
     * Constructs a new board without a password
     * @param title String board title
     */
    public Board(final String title) {
        this.title = title;
        this.columnList = new LinkedList<Column>();
    }

    /**
     * Constructs new board with a password
     * @param title String board title
     * @param password String password
     */
    public Board(final String title, final String password) {
        this.title = title;
        this.password = password;
        this.columnList = new LinkedList<Column>();
    }

    /**
     * Getter for columnList that contains all columns for this board
     * @return List<Column>
     */
    public List<Column> getColumnList() { return columnList; }

    /**
     * Adds columns to columnList
     * @param col Column to add
     */
    public void addColumn(final Column col) { columnList.add(col); }
}
