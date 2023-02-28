package commons;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Tag {
    @Id
    private String title;

    private String hexColor;

    public Tag() {}
    public Tag(String title, String hexColor) {
        this.title = title;
        this.hexColor = hexColor;
    }

    public String getTitle() {
        return this.title;
    }

    public String getHexColor() {
        return this.hexColor;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setHexColor(String hexColor) {
        this.hexColor = hexColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return title.equals(tag.title) && hexColor.equals(tag.hexColor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, hexColor);
    }
}
