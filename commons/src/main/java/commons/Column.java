package commons;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
public class Column implements Comparable<Column> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private long serializationId;

    @Getter
    private long id;

    @Getter @Setter
    @NotNull
    private int index;

    @Getter @Setter
    @NotBlank
    private String heading;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("priority")
    @Getter @Setter
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
     * Constructor for Column (mainly for testing as id is also provided in this)
     * @param id Id of column
     * @param index index of the column
     * @param heading heading of the column
     * @param cards cards contained by the column
     */
    public Column(final long id, final int index, final String heading, final SortedSet<Card> cards) {
        this.id = id;
        this.index = index;
        this.heading = heading;
        this.cards = cards;
    }

    /**
     * Generates a unique id for the column
     * @return generated id
     */
    public long generateId() {
        this.id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        return this.id;
    }

    /**
     * Add one single card to the column if not already in the column and not null
     * @param card Card to be added
     *
     * @return success/failure
     */
    public boolean addCard(final Card card) {
        return card != null && this.cards.add(card);
    }

    /**
     * Remove one card from the column
     * Returns directly if card to be removed is null as TreeSet does not support storing null elements and throws NullPointerException
     * @param card card to remove
     *
     * @return success/failure
     */
    public boolean removeCard(final Card card) {
        return card != null && this.cards.remove(card);
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

    /**
     * Updates the position of the card in the column according to the new position.
     * @param card card to be updated
     * @param newPosition new position of the card
     */
    public void updateCardPosition(final Card card, final int newPosition) {
        this.cards.remove(card);
        card.setPriority(newPosition);
        final List<Card> newOrder = new ArrayList<>();

        for (final Card c:
             this.cards)
        {
            if (c.getPriority() == newPosition)
                newOrder.add(card);
            newOrder.add(c);
        }

        for (int i = 0; i < newOrder.size(); i++) {
            newOrder.get(i).setPriority(i);
        }

        this.cards.add(card);
    }

    /**
     * Updates the card in the column with the values from the given card with the same id
     * @param card card with new values but same id
     */
    public void updateCard(final Card card) {
        for (final Card c : cards) {
            if (c.getId() == card.getId())
                c.update(card);
        }
    }
}
