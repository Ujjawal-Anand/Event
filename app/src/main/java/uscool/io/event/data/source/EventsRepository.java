package uscool.io.event.data.source;

import static com.google.common.base.Preconditions.checkNotNull;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import uscool.io.event.data.Event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by andy1729 on 18/01/18.
 *
 * Concrete implementation to load events from the data sources into a cache.
 * <p>
 * For simplicity, this implements a dumb synchronisation between locally persisted data and data
 * obtained from the server, by using the remote data source only if the local database doesn't
 * exist or is empty.
 */
public class EventsRepository implements EventsDataSource {

    private static EventsRepository INSTANCE = null;

    private final EventsDataSource mEventsRemoteDataSource;
    private final EventsDataSource mEventsLocalDataSource;

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    Map<String, Event> mCachedEvents;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    boolean mCacheIsDirty = false;

    // Prevent direct instantiation.

    private EventsRepository(@NonNull EventsDataSource eventsRemoteDataSource,
                             @NonNull EventsDataSource eventsLocalDataSource) {
        mEventsRemoteDataSource = checkNotNull(eventsRemoteDataSource);
        mEventsLocalDataSource = checkNotNull(eventsLocalDataSource);
    }


    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param eventsRemoteDataSource the backend data source
     * @param eventsLocalDataSource  the device storage data source
     * @return the {@link EventsRepository} instance
     */
    public static EventsRepository getInstance(EventsDataSource eventsRemoteDataSource,
                                               EventsDataSource eventsLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new EventsRepository(eventsRemoteDataSource, eventsLocalDataSource);
        }
        return INSTANCE;
    }

    /**
     * Used to force {@link #getInstance(EventsDataSource, EventsDataSource)} to create a new instance
     * next time it's called.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }

    /**
     * Gets events from cache, local data source (SQLite) or remote data source, whichever is
     * available first.
     * <p>
     * Note: {@link LoadEventsCallback#onDataNotAvailable()} is fired if all data sources fail to
     * get the data.
     */
    @Override
    public void getEvents(@NonNull final LoadEventsCallback callback) {
        checkNotNull(callback);
            // Query the local storage if available. If not, query the network.
            mEventsLocalDataSource.getEvents(new LoadEventsCallback() {
                @Override
                public void onEventsLoaded(List<Event> events) {
                    refreshCache(events);
                    callback.onEventsLoaded(new ArrayList<>(mCachedEvents.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    getEventsFromRemoteDataSource(callback);
                }
            });
    }

    @Override
    public void saveEvent(@NonNull Event event) {
        checkNotNull(event);
        mEventsRemoteDataSource.saveEvent(event);
        mEventsLocalDataSource.saveEvent(event);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedEvents == null) {
            mCachedEvents = new LinkedHashMap<>();
        }
        mCachedEvents.put(event.getId(), event);
    }





    @Override
    public void activateEvent(@NonNull Event event) {
        checkNotNull(event);
        mEventsRemoteDataSource.activateEvent(event);
        mEventsLocalDataSource.activateEvent(event);

        Event activeEvent = new Event(event.getImgFilePath(), event.getDescription(), event.getId());

        // Do in memory cache update to keep the app UI up to date
        if (mCachedEvents == null) {
            mCachedEvents = new LinkedHashMap<>();
        }
        mCachedEvents.put(event.getId(), activeEvent);
    }




    /**
     * Gets events from local data source (sqlite) unless the table is new or empty. In that case it
     * uses the network data source. This is done to simplify the sample.
     * <p>
     * Note: {@link GetEventCallback#onDataNotAvailable()} is fired if both data sources fail to
     * get the data.
     */
    @Override
    public void getEvent(@NonNull final String eventId, @NonNull final GetEventCallback callback) {
        checkNotNull(eventId);
        checkNotNull(callback);

        Event cachedEvent = getEventWithId(eventId);

        // Respond immediately with cache if available

        // Load from server/persisted if needed.

        // Is the event in the local data source? If not, query the network.
        mEventsLocalDataSource.getEvent(eventId, new GetEventCallback() {
            @Override
            public void onEventLoaded(Event event) {
                // Do in memory cache update to keep the app UI up to date
                if (mCachedEvents == null) {
                    mCachedEvents = new LinkedHashMap<>();
                }
                mCachedEvents.put(event.getId(), event);
                callback.onEventLoaded(event);
            }

            @Override
            public void onDataNotAvailable() {
                mEventsRemoteDataSource.getEvent(eventId, new GetEventCallback() {
                    @Override
                    public void onEventLoaded(Event event) {
                        // Do in memory cache update to keep the app UI up to date
                        if (mCachedEvents == null) {
                            mCachedEvents = new LinkedHashMap<>();
                        }
                        mCachedEvents.put(event.getId(), event);
                        callback.onEventLoaded(event);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    @Override
    public void refreshEvents() {
        mCacheIsDirty = true;
    }

    @Override
    public void deleteAllEvents() {
        mEventsRemoteDataSource.deleteAllEvents();
        mEventsLocalDataSource.deleteAllEvents();

        if (mCachedEvents == null) {
            mCachedEvents = new LinkedHashMap<>();
        }
        mCachedEvents.clear();
    }

    @Override
    public void deleteEvent(@NonNull String eventId) {
        mEventsRemoteDataSource.deleteEvent(checkNotNull(eventId));
        mEventsLocalDataSource.deleteEvent(checkNotNull(eventId));

        mCachedEvents.remove(eventId);
    }

    private void getEventsFromRemoteDataSource(@NonNull final LoadEventsCallback callback) {
        mEventsRemoteDataSource.getEvents(new LoadEventsCallback() {
            @Override
            public void onEventsLoaded(List<Event> events) {
                refreshCache(events);
                refreshLocalDataSource(events);
                callback.onEventsLoaded(new ArrayList<>(mCachedEvents.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshCache(List<Event> events) {
        if (mCachedEvents == null) {
            mCachedEvents = new LinkedHashMap<>();
        }
        mCachedEvents.clear();
        for (Event event : events) {
            mCachedEvents.put(event.getId(), event);
        }
        mCacheIsDirty = false;
    }

    private void refreshLocalDataSource(List<Event> events) {
        mEventsLocalDataSource.deleteAllEvents();
        for (Event event : events) {
            mEventsLocalDataSource.saveEvent(event);
        }
    }

    @Nullable
    private Event getEventWithId(@NonNull String id) {
        checkNotNull(id);
        if (mCachedEvents == null || mCachedEvents.isEmpty()) {
            return null;
        } else {
            return mCachedEvents.get(id);
        }
    }
}
