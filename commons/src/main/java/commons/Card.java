package commons;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String title;
    private String description;

    @ManyToMany
    private Set<Tag> tags;

    /**
     * Empty constructor for the Card object
     */
    public Card() {

    }

    /**
     * Constructor for the Card object
     * @param title Card title
     * @param description Card description
     * @param tags Tags assigned to the card
     */
    public Card(final String title, final String description, final Set<Tag> tags) {
        this.title = title;
        this.description = description;
        this.tags = tags;
    }

    /**
     * Getter for the ID
     * @return card ID
     */
    public long getId() {
        return id;
    }

    /**
     * Getter for the card title
     * @return Card title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for card title
     * @param title title to be set to the card
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * Getter for card description
     * @return description of the card
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for card description
     * @param description description for the card to be set
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Getter for the card tags
     * @return Set containing tags assigned to the card
     */
    public Set<Tag> getTags() {
        return tags;
    }

    /**
     * Setter for the tag set
     * @param tags A set containing the tags assigned to the card
     */
    public void setTags(final Set<Tag> tags) {
        this.tags = tags;
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
        return id == card.id && title.equals(card.title) && description.equals(card.description)
                && tags.equals(card.tags);
    }

    /**
     * generates a hash code for the card
     * @return Generated hash code for the card
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, tags);
    }
}
