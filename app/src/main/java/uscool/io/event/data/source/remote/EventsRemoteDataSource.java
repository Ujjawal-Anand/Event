package uscool.io.event.data.source.remote;

import android.os.Handler;
import android.support.annotation.NonNull;

import uscool.io.event.data.Event;
import uscool.io.event.data.source.EventsDataSource;
import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by andy1729 on 18/01/18.
 *
 * Implementation of the data source that adds a latency simulating network.
 */
public class EventsRemoteDataSource implements EventsDataSource {

    private static EventsRemoteDataSource INSTANCE;

    private static final int SERVICE_LATENCY_IN_MILLIS = 5000;

    private final static Map<String, Event> TASKS_SERVICE_DATA;

    static {
        TASKS_SERVICE_DATA = new LinkedHashMap<>(2);
        addEvent("Build tower in Pisa", "Ground looks good, no foundation work required.");
        addEvent("Finish bridge in Tacoma", "Found awesome girders at half the cost!");
    }

    public static EventsRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EventsRemoteDataSource();
        }
        return INSTANCE;
    }

    // Prevent direct instantiation.
    private EventsRemoteDataSource() {}

    private static void addEvent(String title, String description) {
        Event newEvent = new Event(title, description);
        TASKS_SERVICE_DATA.put(newEvent.getId(), newEvent);
    }

    /**
     * Note: {@link LoadEventsCallback#onDataNotAvailable()} is never fired. In a real remote data
     * source implementation, this would be fired if the server can't be contacted or the server
     * returns an error.
     */
    @Override
    public void getEvents(final @NonNull LoadEventsCallback callback) {
        // Simulate network by delaying the execution.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onEventsLoaded(Lists.newArrayList(TASKS_SERVICE_DATA.values()));
            }
        }, SERVICE_LATENCY_IN_MILLIS);
    }

    /**
     * Note: {@link GetEventCallback#onDataNotAvailable()} is never fired. In a real remote data
     * source implementation, this would be fired if the server can't be contacted or the server
     * returns an error.
     */
    @Override
    public void getEvent(@NonNull String eventId, final @NonNull GetEventCallback callback) {
        final Event event = TASKS_SERVICE_DATA.get(eventId);

        // Simulate network by delaying the execution.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onEventLoaded(event);
            }
        }, SERVICE_LATENCY_IN_MILLIS);
    }

    @Override
    public void saveEvent(@NonNull Event event) {
        TASKS_SERVICE_DATA.put(event.getId(), event);
    }

    @Override
    public void completeEvent(@NonNull Event event) {
        Event completedEvent = new Event(event.getTitle(), event.getDescription(), event.getId(), true);
        TASKS_SERVICE_DATA.put(event.getId(), completedEvent);
    }

    @Override
    public void completeEvent(@NonNull String eventId) {
        // Not required for the remote data source because the {@link EventsRepository} handles
        // converting from a {@code eventId} to a {@link event} using its cached data.
    }

    @Override
    public void activateEvent(@NonNull Event event) {
        Event activeEvent = new Event(event.getTitle(), event.getDescription(), event.getId());
        TASKS_SERVICE_DATA.put(event.getId(), activeEvent);
    }

    @Override
    public void activateEvent(@NonNull String eventId) {
        // Not required for the remote data source because the {@link EventsRepository} handles
        // converting from a {@code eventId} to a {@link event} using its cached data.
    }

    @Override
    public void clearCompletedEvents() {
        Iterator<Map.Entry<String, Event>> it = TASKS_SERVICE_DATA.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Event> entry = it.next();
            if (entry.getValue().isCompleted()) {
                it.remove();
            }
        }
    }

    @Override
    public void refreshEvents() {
        // Not required because the {@link EventsRepository} handles the logic of refreshing the
        // events from all the available data sources.
    }

    @Override
    public void deleteAllEvents() {
        TASKS_SERVICE_DATA.clear();
    }

    @Override
    public void deleteEvent(@NonNull String eventId) {
        TASKS_SERVICE_DATA.remove(eventId);
    }
}
