package commons;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

public class SubTask {

    @Getter @Setter
    private String description;

    @Getter @Setter
    private boolean done;

    /**
     * equals method for SubTask
     * @param o object to compare this with
     * @return o == this ?
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final SubTask subTask = (SubTask) o;

        if (done != subTask.done) return false;
        return Objects.equals(description, subTask.description);
    }

    /**
     * hash code for SubTask
     * @return hashcode
     */
    @Override
    public int hashCode() {
        int result = description != null ? description.hashCode() : 0;
        result = 31 * result + (done ? 1 : 0);
        return result;
    }
}
