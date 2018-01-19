package uscool.io.event.data.source.remote;

import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;

import uscool.io.event.R;
import uscool.io.event.data.Event;
import uscool.io.event.data.source.EventsDataSource;
import uscool.io.event.util.AppUtil;

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
        addEvent("andy1729", "null","Hello Android!");
        addEvent("Ujjawal Anand","null","Hello World from Ujjawal Anand!");

    }

    public static EventsRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EventsRemoteDataSource();
        }
        return INSTANCE;
    }

    // Prevent direct instantiation.
    private EventsRemoteDataSource() {}

    private static void addEvent(String username, String filePath, String description) {
        Event newEvent = new Event(username, filePath, description);
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
    public void activateEvent(@NonNull Event event) {
        Event activeEvent = new Event(event.getImgFilePath(), event.getDescription(), event.getId());
        TASKS_SERVICE_DATA.put(event.getId(), activeEvent);
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
