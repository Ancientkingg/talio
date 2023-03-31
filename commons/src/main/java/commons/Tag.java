package commons;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Objects;

@Entity
public class Tag {
    @Id
    @Getter @Setter
    private String title;

    @OneToOne
    @Getter @Setter
    private ColorScheme colorScheme;

    /**
     * Empty constructor for the Tag object
     */
    protected Tag() {

    }

    /**
     * Constructor for the Tag object
     * @param title Title of the tag
     * @param hexColor Color of the tag in hex notation
     */
    public Tag(final String title, final ColorScheme hexColor) {
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
        return title.equals(tag.title) && colorScheme.equals(tag.colorScheme);
    }


    /**
     * Generates a hash code
     * @return Returns a hash code of the tag object
     */
    @Override
    public int hashCode() {
        return Objects.hash(title, colorScheme);
    }
}
