package commons;

import javax.persistence.Entity;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Board {
    @Id
    String joinKey;
    Timestamp created;
    String passwords;
    @OneToMany
    Set<CardList> columns;

    public Board() {
        this.created = new Timestamp(System.currentTimeMillis());
    }

    public Board(String joinKey, String passwords, Set<CardList> columns) {
        this.joinKey = joinKey;
        this.passwords = passwords;
        this.columns = columns;
        this.created = new Timestamp(System.currentTimeMillis());
    }

    public String getJoinKey() {
        return joinKey;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public String getPasswords() {
        return passwords;
    }

    public void setPasswords(String passwords) {
        this.passwords = passwords;
    }

    public Set<CardList> getColumns() {
        return columns;
    }

    public void setColumns(Set<CardList> columns) {
        this.columns = columns;
    }

    public boolean addList(CardList cardList) {
        return this.columns.add(cardList);
    }

    public boolean removeList(CardList cardList) {
        return this.columns.remove(cardList);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return joinKey.equals(board.joinKey) && created.equals(board.created) && passwords.equals(board.passwords) && columns.equals(board.columns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(joinKey, created, passwords, columns);
    }
}
