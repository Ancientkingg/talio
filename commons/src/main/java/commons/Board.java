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
    private String password;
    @OneToMany
    private Set<Column> columns;

    /**
     * Constructor for the board object.
     * Sets the created date to now
     */
    private Board() {
        this.created = new Timestamp(System.currentTimeMillis());
    }

    /**
     * Constructor for the board object
     * Sets the created date to now
     * @param joinKey Key for joining
     * @param password password for the board
     * @param columns A set containing the board columns
     */
    public Board(final String joinKey, final String password, final Set<Column> columns) {
        this.joinKey = joinKey;
        this.password = password;
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
    public String getPassword() {
        return password;
    }

    /**
     * Setter for a new password
     * @param passwords password to be set
     */
    public void setPassword(final String passwords) {
        this.password = passwords;
    }

    /**
     * Getter for the board columns
     * @return Set containing CardLists
     */
    public Set<Column> getColumns() {
        return columns;
    }

    /**
     * Setter for the board columns
     * @param columns Set containing CardLists
     */
    public void setColumns(final Set<Column> columns) {
        this.columns = columns;
    }

    /**
     * Add one single list to the board if not already in the board
     * @param cardList Column object
     *
     * @return success/failure
     */
    public boolean addList(final Column cardList) {
        return this.columns.add(cardList);
    }

    /**
     * Remove one list from the board
     * @param cardList List to remove
     *
     * @return success/failure
     */
    public boolean removeList(final Column cardList) {
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
        return joinKey.equals(board.joinKey) && created.equals(board.created) && password.equals(board.password) && columns.equals(board.columns);
    }

    /**
     * Generator for the board hash code
     * @return Generated hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(joinKey, created, password, columns);
    }
}
