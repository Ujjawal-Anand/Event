package uscool.io.event.home;

/**
 * Created by andy1729 on 18/01/18.
 *
 * Used with the filter spinner in the tasks list.
 */
public enum EventsFilterType {
    /**
     * Do not filter tasks.
     */
    ALL_TASKS,

    /**
     * Filters only the active (not completed yet) tasks.
     */
    ACTIVE_TASKS,

    /**
     * Filters only the completed tasks.
     */
    COMPLETED_TASKS
}
