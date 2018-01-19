package uscool.io.event.util;

import android.content.Context;
import android.support.annotation.NonNull;

import uscool.io.event.data.source.EventsRepository;
import uscool.io.event.data.source.local.EventDatabase;
import uscool.io.event.data.source.local.EventsLocalDataSource;
import uscool.io.event.data.source.remote.EventsRemoteDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by andy1729 on 18/01/18.
 *
 */

public class Injection {

    public static EventsRepository provideEventsRepository(@NonNull Context context) {
        checkNotNull(context);
        EventDatabase database = EventDatabase.getInstance(context);
        return EventsRepository.getInstance(EventsRemoteDataSource.getInstance(),
                EventsLocalDataSource.getInstance(new AppExecutors(),
                        database.eventDao()));
    }

}