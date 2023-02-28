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
    private String joinKey;
    private Timestamp created;
    private String passwords;
    @OneToMany
    private Set<CardList> columns;

    /**
     * Constructor for the board object.
     * Sets the created date to now
     */
    public Board() {
        this.created = new Timestamp(System.currentTimeMillis());
    }

    /**
     * Constructor for the board object
     * Sets the created date to now
     * @param joinKey Key for joining
     * @param passwords password for the board
     * @param columns A set containing the board columns
     */
    public Board(final String joinKey, final String passwords, final Set<CardList> columns) {
        this.joinKey = joinKey;
        this.passwords = passwords;
        this.columns = columns;
        this.created = new Timestamp(System.currentTimeMillis());
    }

    /**
     * Getter for the board join key
     * @return Board join key
     */
    public String getJoinKey() {
        return joinKey;
    }

    /**
     * Getter for the creation date
     * @return the date the board was created
     */
    public Timestamp getCreated() {
        return created;
    }

    /**
     * Getter for the board password
     * @return Board password
     */
    public String getPasswords() {
        return passwords;
    }

    /**
     * Setter for a new password
     * @param passwords password to be set
     */
    public void setPasswords(final String passwords) {
        this.passwords = passwords;
    }

    /**
     * Getter for the board columns
     * @return Set containing CardLists
     */
    public Set<CardList> getColumns() {
        return columns;
    }

    /**
     * Setter for the board columns
     * @param columns Set containing CardLists
     */
    public void setColumns(final Set<CardList> columns) {
        this.columns = columns;
    }

    /**
     * Add one single list to the board if not already in the board
     * @param cardList CardList object
     *
     * @return success/failure
     */
    public boolean addList(final CardList cardList) {
        return this.columns.add(cardList);
    }

    /**
     * Remove one list from the board
     * @param cardList List to remove
     *
     * @return success/failure
     */
    public boolean removeList(final CardList cardList) {
        return this.columns.remove(cardList);
    }

    /**
     * Checks for equality between two boards
     * @param o Other board
     *
     * @return is this board equal to the other board?
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Board board = (Board) o;
        return joinKey.equals(board.joinKey) && created.equals(board.created) && passwords.equals(board.passwords) && columns.equals(board.columns);
    }

    /**
     * Generator for the board hash code
     * @return Generated hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(joinKey, created, passwords, columns);
    }
}
