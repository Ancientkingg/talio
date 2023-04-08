package commons;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;
import java.util.UUID;

@Entity
public class SubTask {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private long serializationId;

    @Getter
    private long id;

    @Getter @Setter
    private String description;

    @Getter @Setter
    private boolean isDone;

    /**
     * Constructor for SubTask
     * @param description description of the SubTask
     * @param isDone whether the task is done or not
     *
     * id set to a randomly generated id
     */
    public SubTask(final String description, final boolean isDone) {
        this.description = description;
        this.isDone = isDone;
        this.id = generateId();
    }

    /**
     * Dummy constructor made to keep checkstyle happy
     */
    public SubTask() {

    }

    /**
     * Generates a unique id for the subtask
     * @return generated id
     */
    public long generateId() {
        this.id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        return this.id;
    }

    /**
     * equals method for subtask
     * @param o SubTask to compare this with
     * @return this == o ?
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final SubTask subTask = (SubTask) o;

        if (id != subTask.id) return false;
        if (isDone != subTask.isDone) return false;
        return Objects.equals(description, subTask.description);
    }


    /**
     * hashcode for SubTask
     * @return hashcode
     */
    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (isDone ? 1 : 0);
        return result;
    }
}
