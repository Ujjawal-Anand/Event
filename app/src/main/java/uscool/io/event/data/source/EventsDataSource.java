package uscool.io.event.data.source;

import android.support.annotation.NonNull;

import uscool.io.event.data.Event;

import java.util.List;

/**
 * Created by andy1729 on 18/01/18.
 *
 * Main entry point for accessing events data.
 * <p>
 * For simplicity, only getEvents() and getEvent() have callbacks. Consider adding callbacks to other
 * methods to inform the user of network/database errors or successful operations.
 * For example, when a new event is created, it's synchronously stored in cache but usually every
 * operation on database or network should be executed in a different thread.
 */
public interface EventsDataSource {

    interface LoadEventsCallback {

        void onEventsLoaded(List<Event> events);

        void onDataNotAvailable();
    }

    interface GetEventCallback {

        void onEventLoaded(Event event);

        void onDataNotAvailable();
    }

    void getEvents(@NonNull LoadEventsCallback callback);

    void getEvent(@NonNull String eventId, @NonNull GetEventCallback callback);

    void saveEvent(@NonNull Event event);

    void completeEvent(@NonNull Event event);

    void completeEvent(@NonNull String eventId);

    void activateEvent(@NonNull Event event);

    void activateEvent(@NonNull String eventId);

    void clearCompletedEvents();

    void refreshEvents();

    void deleteAllEvents();

    void deleteEvent(@NonNull String eventId);
}
