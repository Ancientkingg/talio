package commons;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private long serializationId;

    @Getter
    private final long id;

    @Getter @Setter
    private String title;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter @Setter
    private ColorScheme colorScheme;

    /**
     * Empty constructor for the Tag object
     */
    protected Tag() {
        this.id = (long) (Math.random() * 1000000000);
    }

    /**
     * Constructor for the Tag object
     * @param title Title of the tag
     * @param hexColor Color of the tag in hex notation
     */
    public Tag(final String title, final ColorScheme hexColor) {
        this.id = (long) (Math.random() * 1000000000);
        this.title = title;
        this.colorScheme = hexColor;
    }

    public Tag(final long id, final String title, final ColorScheme hexColor) {
        this.id = id;
        this.title = title;
        this.colorScheme = hexColor;
    }

    /**
     * Checks for equality between Tags
     * @param o Another Tag object
     *
     * @return Equality of this Tag and the other tag
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Tag tag = (Tag) o;
        return id == tag.id;
    }


    /**
     * Generates a hash code
     * @return Returns a hash code of the tag object
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
