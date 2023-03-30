package commons;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.awt.*;
import java.util.Objects;

@Entity
public class ColorScheme {

    @Id @Getter @Setter
    private Color textColor;

    @Id @Getter @Setter
    private Color backgroundColor;

    /**
     * Constructor for ColorScheme
     * @param textColor color for text (called font color in backlog)
     * @param backgroundColor color for background
     */
    public ColorScheme(final Color textColor, final Color backgroundColor) {
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
    }

    /**
     * for checkstyle
     */
    public ColorScheme() {

    }

    /**
     * Equals method for ColorScheme
     * @param o to compare with this
     * @return this == o ?
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ColorScheme that = (ColorScheme) o;

        if (!Objects.equals(textColor, that.textColor)) return false;
        return Objects.equals(backgroundColor, that.backgroundColor);
    }

    /**
     * hashcode for ColorScheme
     * @return hash code
     */
    @Override
    public int hashCode() {
        int result = textColor != null ? textColor.hashCode() : 0;
        result = 31 * result + (backgroundColor != null ? backgroundColor.hashCode() : 0);
        return result;
    }
}
