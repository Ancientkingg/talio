package commons;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private long serializationId;

    @Getter
    @Setter
    private int red;

    @Getter
    @Setter
    private int green;

    @Getter
    @Setter
    private int blue;

    @Getter
    @Setter
    private int alpha;

    /**
     * Constructor for Color
     * @param red red value
     * @param green green value
     * @param blue blue value
     * @param alpha alpha value
     */
    public Color(final int red, final int green, final int blue, final int alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    /**
     * Constructor for Color from hex string
     * @param hexColor hex color string
     */
    public Color (final String hexColor) {
        this.red = Integer.parseInt(hexColor.substring(1, 3), 16);
        this.green = Integer.parseInt(hexColor.substring(3, 5), 16);
        this.blue = Integer.parseInt(hexColor.substring(5, 7), 16);
        this.alpha = 255;
    }

    /**
     * for checkstyle
     */
    protected Color() {

    }

    /**
     * equals method for Color
     * @param o to compare with this
     * @return this == o ?
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Color color = (Color) o;
        return red == color.red && green == color.green && blue == color.blue && alpha == color.alpha;
    }

    /**
     * hashcode for Color
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(red, green, blue, alpha);
    }

    /**
     * toString for Color
     * @return string representation of Color
     */
    @Override
    public String toString() {
        return "rgba(" + red + ", " + green + ", " + blue + ", " + alpha / 255.0 + ")";
    }
}
