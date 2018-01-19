package uscool.io.event.data.source.local;

import static com.google.common.base.Preconditions.checkNotNull;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import uscool.io.event.data.Event;
import uscool.io.event.data.source.EventsDataSource;
import uscool.io.event.util.AppExecutors;

import java.util.List;


/**
 * Concrete implementation of a data source as a db.
 */
public class EventsLocalDataSource implements EventsDataSource {

    private static volatile EventsLocalDataSource INSTANCE;

    private EventsDao mEventsDao;

    private AppExecutors mAppExecutors;

    // Prevent direct instantiation.
    private EventsLocalDataSource(@NonNull AppExecutors appExecutors,
                                  @NonNull EventsDao eventsDao) {
        mAppExecutors = appExecutors;
        mEventsDao = eventsDao;
    }

    public static EventsLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                    @NonNull EventsDao eventsDao) {
        if (INSTANCE == null) {
            synchronized (EventsLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new EventsLocalDataSource(appExecutors, eventsDao);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Note: {@link LoadEventsCallback#onDataNotAvailable()} is fired if the database doesn't exist
     * or the table is empty.
     */
    @Override
    public void getEvents(@NonNull final LoadEventsCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Event> events = mEventsDao.getEvents();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (events.isEmpty()) {
                            // This will be called if the table is new or just empty.
                            callback.onDataNotAvailable();
                        } else {
                            callback.onEventsLoaded(events);
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    /**
     * Note: {@link GetEventCallback#onDataNotAvailable()} is fired if the {@link Event} isn't
     * found.
     */
    @Override
    public void getEvent(@NonNull final String eventId, @NonNull final GetEventCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final Event event = mEventsDao.getEventById(eventId);

                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (event != null) {
                            callback.onEventLoaded(event);
                        } else {
                            callback.onDataNotAvailable();
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void saveEvent(@NonNull final Event event) {
        checkNotNull(event);
        Runnable saveRunnable = new Runnable() {
            @Override
            public void run() {
                mEventsDao.insertEvent(event);
            }
        };
        mAppExecutors.diskIO().execute(saveRunnable);
    }



    @Override
    public void activateEvent(@NonNull final Event event) {
    /*    Runnable activateRunnable = new Runnable() {
            @Override
            public void run() {
                mEventsDao.updateCompleted(event.getId(), false);
            }
        };
        mAppExecutors.diskIO().execute(activateRunnable);*/
    }


    @Override
    public void refreshEvents() {
        // Not required because the {@link EventsRepository} handles the logic of refreshing the
        // events from all the available data sources.
    }

    @Override
    public void deleteAllEvents() {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                mEventsDao.deleteEvents();
            }
        };

        mAppExecutors.diskIO().execute(deleteRunnable);
    }

    @Override
    public void deleteEvent(@NonNull final String eventId) {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                mEventsDao.deleteEventById(eventId);
            }
        };

        mAppExecutors.diskIO().execute(deleteRunnable);
    }

    @VisibleForTesting
    static void clearInstance() {
        INSTANCE = null;
    }
}
