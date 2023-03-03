package client.items;

import java.util.List;

public class Column {

    private String title;
    private List taskList;

    public Column(String title){
        this.title = title;
    }

    public String getTitle(){ return title;}
}
