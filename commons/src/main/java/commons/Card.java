package commons;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Card implements Comparable<Card> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private long id;

    @Getter @Setter
    private String title;
    @Getter @Setter
    private int priority;
    @Getter @Setter
    private String description;

    @ManyToMany
    @Getter @Setter
    private Set<Tag> tags;

    /**
     * Empty constructor for the Card object
     */
    protected Card() {

    }

    /**
     * Constructor for the Card object
     * @param title Card title
     * @param priority Card priority
     * @param description Card description
     * @param tags Tags assigned to the card
     */
    public Card(final String title, final int priority, final String description, final Set<Tag> tags) {
        this.title = title;
        this.priority = priority;
        this.description = description;
        this.tags = tags == null ? new HashSet<>(0) : tags;
    }

    /**
     * Constructor for the Card object with specified id
     * @param id Card id
     * @param title Card title
     * @param priority Card priority
     * @param description Card description
     * @param tags Tags assigned to the card
     */
    public Card(final long id, final String title, final int priority, final String description, final Set<Tag> tags) {
        this(title, priority, description, tags);
        this.id = id;
    }

    /**
     * Assign one single tag to the card if not already assigned
     * @param tag tag to assign
     *
     * @return success/failure
     */
    public boolean addTag(final Tag tag) {
        return this.tags.add(tag);
    }

    /**
     * Remove one tag from the card if the tag is assigned to the card
     * @param tag tag to be removed
     *
     * @return success/failure
     */
    public boolean removeTag(final Tag tag) {
        return this.tags.remove(tag);
    }

    /**
     * Checks for equality of two card objects
     * @param o Other card
     *
     * @return if this card equal to the other card?
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Card card = (Card) o;
        return id == card.id && title.equals(card.title) && priority == card.priority
                && description.equals(card.description) && tags.equals(card.tags);
    }

    /**
     * generates a hash code for the card
     * @return Generated hash code for the card
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, title, priority, description, tags);
    }

    /**
     * Compares two cards on the basis of their priority
     * @param o the object to be compared.
     *
     * @return 0 if the objects have the same priority,
     * 1 if this object has a higher priority,
     * -1 if the other object has a higher priority
     */
    @Override
    public int compareTo(final Card o) {
        return Integer.compare(this.priority, o.priority);
    }

    /**
     * Updates the card with the values from another card
     * @param card Card to copy from
     */
    public void update(final Card card) {
        this.title = card.title;
        this.priority = card.priority;
        this.description = card.description;
        this.tags = card.tags;
    }
}