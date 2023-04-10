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
public class SubTask implements Comparable<SubTask> {

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

    @Getter @Setter
    private int priority;


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
     * Constructor for SubTask (mainly for testing)
     * @param description description of the SubTask
     * @param isDone whether the task is done or not
     * @param id id of subtask
     */
    public SubTask(final String description, final boolean isDone, final long id) {
        this.description = description;
        this.isDone = isDone;
        this.id = id;
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
     * @param o object to compare to
     *
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SubTask subTask = (SubTask) o;
        return serializationId == subTask.serializationId && id == subTask.id
                && isDone == subTask.isDone && priority == subTask.priority
                && Objects.equals(description, subTask.description);
    }

    /**
     * @return hashcode of the object
     */
    @Override
    public int hashCode() {
        return Objects.hash(serializationId, id, description, isDone, priority);
    }

    /**
     * @param o the object to be compared.
     *
     * @return 0 if the objects are equal,
     * -1 if this object is less than the other object,
     * 1 if this object is greater than the other object
     */
    @Override
    public int compareTo(final SubTask o) {
        if (this.isDone && !o.isDone) {
            return 1;
        } else if (!this.isDone && o.isDone) {
            return -1;
        } else {
            return Integer.compare(this.priority, o.priority);
        }
    }
}
