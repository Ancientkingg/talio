package commons;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity
public class Card implements Comparable<Card> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private long serializationId;

    @Getter
    private long id;

    @Getter @Setter
    private String title;

    // priorities start from 0
    @Getter @Setter
    private int priority;
    @Getter @Setter
    private String description;

    @OrderColumn
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy("priority")
    @Getter @Setter
    private SortedSet<SubTask> subtasks;

    @Getter @Setter
    private Boolean isDefaultThemed;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @Getter @Setter
    private ColorScheme colorScheme;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Getter @Setter
    private Set<Tag> tags;

    /**
     * Empty constructor for the Card object
     */
    protected Card() {

    }

    /**
     * Constructor for the Card object
     * gives default color scheme
     *
     * @param title Card title
     * @param priority Card priority
     * @param description Card description
     * @param subTasks List of subtasks for the card
     * @param tags Tags assigned to the card
     */
    public Card(final String title, final int priority, final String description, final SortedSet<SubTask> subTasks, final Set<Tag> tags) {
        this.title = title;
        this.priority = priority;
        this.description = description;
        this.subtasks = subTasks == null ? new TreeSet<>() : subTasks;
        this.tags = tags == null ? new HashSet<>(0) : tags;
    }

    /**
     * Constructor for the Card object with specified id
     * @param id Card id
     * @param title Card title
     * @param priority Card priority
     * @param description Card description
     * @param subTasks List of subtasks for the card
     * @param tags Tags assigned to the card
     */
    public Card(final long id, final String title, final int priority, final String description, final SortedSet<SubTask> subTasks, final Set<Tag> tags) {
        this(title, priority, description, subTasks, tags);
        this.id = id;
    }

    /**
     * Constructor for the Card object without subtasks
     * @param title Card title
     * @param priority Card priority
     * @param description Card description
     * @param tags Tags assigned to the card
     */
    public Card(final String title, final int priority, final String description, final Set<Tag> tags) {
        this.title = title;
        this.priority = priority;
        this.description = description;
        this.subtasks = new TreeSet<>();
        this.tags = tags == null ? new HashSet<>(0) : tags;

        this.isDefaultThemed = true;
        this.colorScheme = null;
    }

    /**
     * Constructor for the Card object with specified id and without subtasks
     * @param id Card id
     * @param title Card title
     * @param priority Card priority
     * @param description Card description
     * @param tags Tags assigned to the card
     */
    public Card(final long id, final String title, final int priority, final String description, final Set<Tag> tags) {
        this(title, priority, description, tags);
        this.id = id;
    }

     /**
     * Constructor for the Card object
     *
     * @param title Card title
     * @param priority Card priority
     * @param description Card description
     * @param tags Tags assigned to the card
     * @param colorScheme ColorScheme to be used by the card
     */
    public Card(final String title, final int priority, final String description, final Set<Tag> tags, final ColorScheme colorScheme) {
        this.title = title;
        this.priority = priority;
        this.description = description;
        this.tags = tags == null ? new HashSet<>(0) : tags;
        this.isDefaultThemed = false;
        this.colorScheme = colorScheme;
    }


        /**
         * Assign one single tag to the card if not already assigned
         * @param tag tag to assign
         *
         * @return success/failure
         */
    public boolean addTag(final Tag tag) {
        return this.tags.add(tag);
    }

    /**
     * Remove one tag from the card if the tag is assigned to the card
     * @param tag tag to be removed
     *
     * @return success/failure
     */
    public boolean removeTag(final Tag tag) {
        return this.tags.remove(tag);
    }

    /**
     * Add one task to the card
     * @param subtask sub-task to be added
     *
     * @return success/failure
     */
    public boolean addSubTask(final SubTask subtask) {
        return subtasks.add(subtask);
    }

    /**
     * Remove one sub-task from the card if the sub-task is a part of the card
     * @param subtask tag to be removed
     *
     * @return success/failure
     */
    public boolean removeSubTask(final SubTask subtask) {
        return subtasks.remove(subtask);
    }


    /**
     * Counts the number of completed subtasks
     * @return number of completed subtasks
     */
    public int countFinishedSubtasks() {
        int i = 0;
        for (final SubTask task : this.getSubtasks()) {
            if (task.isDone()) {
                i++;
            }
        }
        return i;
    }

    /**
     * Generates a unique id for the card
     * @return generated id
     */
    public long generateId() {
        this.id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        return this.id;
    }

    /**
     * Checks for equality of two card objects
     * @param o Other card
     *
     * @return if this card equal to the other card?
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Card card = (Card) o;
        return id == card.id && title.equals(card.title) && priority == card.priority
                && description.equals(card.description) && subtasks.equals(card.subtasks) && tags.equals(card.tags);
    }

    /**
     * generates a hash code for the card
     * @return Generated hash code for the card
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, title, priority, description, tags);
    }

    /**
     * Compares two cards on the basis of their priority
     * @param o the object to be compared.
     *
     * @return 0 if the objects have the same priority,
     * 1 if this object has a higher priority,
     * -1 if the other object has a higher priority
     */
    @Override
    public int compareTo(final Card o) {
        return Integer.compare(this.priority, o.priority);
    }

    /**
     * Updates the card with the values from another card
     * @param card Card to copy from
     */
    public void update(final Card card) {
        this.title = card.title;
        this.priority = card.priority;
        this.description = card.description;
        this.subtasks = card.subtasks;
        this.tags = card.tags;
        this.isDefaultThemed = card.isDefaultThemed;
        this.colorScheme = card.colorScheme;
    }

    /**
     * Updates the subtask with the values from another subtask
     * @param subTask Subtask to copy from
     */
    public void updateSubTask(final SubTask subTask) {
        for (final SubTask task : this.subtasks) {
            if (task.getId() == subTask.getId()) {
                task.setDescription(subTask.getDescription());
            }
        }
    }

    /**
     * Moves a subtask to a new index
     * @param subTask Subtask to be moved
     * @param index New index of the subtask
     */
    public void moveSubTask(final SubTask subTask, final int index) {
        this.subtasks.stream().filter(s -> s.getId() == subTask.getId()).findFirst()
                .ifPresentOrElse(subtasks::remove, () -> {
                    throw new IllegalArgumentException("Subtask not found");
                });

        int i = 0;
        for (final SubTask task : this.subtasks) {
            task.setPriority(i++);
        }

        if (index >= this.subtasks.size()) {
            this.subtasks.add(subTask);
            return;
        }

        for (final SubTask task : this.subtasks) {
            if (task.getPriority() >= index) {
                task.setPriority(task.getPriority() + 1);
            }
        }

        this.subtasks.add(subTask);

        i = 0;
        for (final SubTask task : this.subtasks) {
            task.setPriority(i++);
        }
    }

    /**
     * Toggles the done status of a subtask
     * @param subTask Subtask to be toggled
     */
    public void toggleSubtask(final SubTask subTask) {
        for (final SubTask task : this.subtasks) {
            if (task.getId() == subTask.getId()) {
                task.setDone(!task.isDone());
            }
        }
    }
}
