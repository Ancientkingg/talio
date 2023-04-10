package commons.DTOs;

import commons.SubTask;

public record SubTaskDTO(SubTask subTask, long cardId, int index, String password) {

    /**
     * Constructor for subtask dto
     * @param subTask subtask
     * @param cardId id of card containing the subtask
     */
    public SubTaskDTO(final SubTask subTask, final long cardId, final String password) {
        this(subTask, cardId, 0, password);
    }

    /**
     * Constructor for subtask dto
     * @param subTask subtask
     * @param cardId id of card containing the subtask
     * @param index index of subtask in card
     */
    public SubTaskDTO(final SubTask subTask, final long cardId, final int index) {
        this(subTask, cardId, index, null);
    }

    /**
     * Constructor for subtask dto
     * @param subTask subtask
     * @param cardId id of card containing the subtask
     */
    public SubTaskDTO(final SubTask subTask, final long cardId) {
        this(subTask, cardId, 0, null);
    }
}
