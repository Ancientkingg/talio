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

    public Card() {}

    public Card(String title, String description, Set<Tag> tags) {
        this.title = title;
        this.description = description;
        this.tags = tags;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public boolean addTag(Tag tag) {
        return this.tags.add(tag);
    }

    public boolean removeTag(Tag tag) {
        return this.tags.remove(tag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return id == card.id && title.equals(card.title) && description.equals(card.description)
                && tags.equals(card.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, tags);
    }
}
