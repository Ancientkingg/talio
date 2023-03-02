package commons;

import javax.persistence.*;
import java.util.*;

@Entity
public class Column implements Comparable<Column> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private int index;

    private String heading;
    @OneToMany
    @OrderBy("priority")
    private SortedSet<Card> cards;

    /**
     * Empty constructor for the Column
     */
    protected Column() {

    }

    /**
     * Constructor for the Column/Column object
     * @param heading Heading for the column
     * @param index Index of the column in the board
     * @param cards A set of cards contained by the column
     */
    public Column(final String heading, final int index, final SortedSet<Card> cards) {
        this.heading = heading;
        this.index = index;
        this.cards = cards;
    }

    /**
     * Getter for ID
     * @return column ID
     */
    public long getId() {
        return id;
    }

    /**
     * Getter for column index
     * @return column index in the board
     */
    public int getIndex() {
        return index;
    }

    /**
     * Setter for column index
     * @param index new index in the board
     */
    public void setIndex(final int index) {
        this.index = index;
    }

    /**
     * Getter for the heading
     * @return column heading
     */
    public String getHeading() {
        return heading;
    }

    /**
     * Setter for the column heading
     * @param heading Heading to be set
     */
    public void setHeading(final String heading) {
        this.heading = heading;
    }

    /**
     * Getter for the cards in the column
     * @return column cards
     */
    public SortedSet<Card> getCards() {
        return cards;
    }

    /**
     * Setter for the column cards
     * @param cards A set containing the cards to be set in the column
     */
    public void setCards(final SortedSet<Card> cards) {
        this.cards = cards;
    }

    /**
     * Add one single card to the column if not already in the column
     * @param card Card to be added
     *
     * @return success/failure
     */
    public boolean addCard(final Card card) {
        return this.cards.add(card);
    }

    /**
     * Remove one card from the column
     * @param card card to remove

     * @return success/failure
     */
    public boolean removeCard(final Card card) {
        return this.cards.remove(card);
    }

    /**
     * Checks for equality of two columns
     * @param o Other column
     *
     * @return is this column equal to the other column?
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Column column = (Column) o;
        return id == column.id && index == column.index
                && heading.equals(column.heading) && cards.equals(column.cards);
    }

    /**
     * Hash code generator for the column
     * @return generated hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, index, heading, cards);
    }

    /**
     * Compares two columns on the basis of
     * their index in the board
     * @param o the object to be compared.
     *
     * @return 0 if the objects have the same index,
     * 1 if this object has a higher index,
     * -1 if the other object has a higher index
     */
    @Override
    public int compareTo(final Column o) {
        return Integer.compare(index, o.index);
    }
}
