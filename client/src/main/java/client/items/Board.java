package client.items;

import java.util.LinkedList;
import java.util.List;

public class Board {

    private String title;
    private String password;
    private List<Column> columnList;

    public Board(String title){
        this.title = title;
        this.columnList = new LinkedList<Column>();
    }

    public void Board(String title, String password){
        this.title = title;
        this.password = password;
        this.columnList = new LinkedList<Column>();
    }

    public List<Column> getColumnList() { return columnList; }

    public void addColumn(Column col) { columnList.add(col); }
}
