package uscool.io.event.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import uscool.io.event.data.Event;

import java.util.List;

/**
 * Created by andy1729 on 18/01/18.
 *
 * Data Access Object for the events table.
 */
@Dao
public interface EventsDao {

    /**
     * Select all events from the events table.
     *
     * @return all events.
     */
    @Query("SELECT * FROM Events")
    List<Event> getEvents();

    /**
     * Select a event by id.
     *
     * @param eventId the event id.
     * @return the event with eventId.
     */
    @Query("SELECT * FROM Events WHERE entryid = :eventId")
    Event getEventById(String eventId);

    /**
     * Insert a event in the database. If the event already exists, replace it.
     *
     * @param event the event to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEvent(Event event);

    /**
     * Update a event.
     *
     * @param event event to be updated
     * @return the number of events updated. This should always be 1.
     */
    @Update
    int updateEvent(Event event);

    /**
     * Update the complete status of a event
     *
     * @param eventId    id of the event
     * @param completed status to be updated
     */
    @Query("UPDATE Events SET completed = :completed WHERE entryid = :eventId")
    void updateCompleted(String eventId, boolean completed);

    /**
     * Delete a event by id.
     *
     * @return the number of events deleted. This should always be 1.
     */
    @Query("DELETE FROM Events WHERE entryid = :eventId")
    int deleteEventById(String eventId);

    /**
     * Delete all events.
     */
    @Query("DELETE FROM Events")
    void deleteEvents();

    /**
     * Delete all completed events from the table.
     *
     * @return the number of events deleted.
     */
    @Query("DELETE FROM Events WHERE completed = 1")
    int deleteCompletedEvents();
}
