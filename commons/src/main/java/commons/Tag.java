package commons;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Tag {
    @Id
    @Getter @Setter
    private String title;

    @Getter @Setter
    private String hexColor;

    /**
     * Empty constructor for the Tag object
     */
    private Tag() {

    }

    /**
     * Constructor for the Tag object
     * @param title Title of the tag
     * @param hexColor Color of the tag in hex notation
     */
    public Tag(final String title, final String hexColor) {
        this.title = title;
        this.hexColor = hexColor;
    }

    /**
     * Checks for equality between Tags
     * @param o An other Tag object
     *
     * @return Equality of this Tag and the other tag
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Tag tag = (Tag) o;
        return title.equals(tag.title) && hexColor.equals(tag.hexColor);
    }

    /**
     * Generates a hash code
     * @return Returns a hash code of the tag object
     */
    @Override
    public int hashCode() {
        return Objects.hash(title, hexColor);
    }
}
