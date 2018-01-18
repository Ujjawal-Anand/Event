package uscool.io.event.data.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import uscool.io.event.data.Event;

/**
 * Created by andy1729 on 18/01/18.
 *
 * The Room Database that contains the Event table.
 */
@Database(entities = {Event.class}, version = 1)
public abstract class EventDatabase extends RoomDatabase {

    private static EventDatabase INSTANCE;

    public abstract EventsDao eventDao();

    private static final Object sLock = new Object();

    public static EventDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        EventDatabase.class, "Events.db")
                        .build();
            }
            return INSTANCE;
        }
    }

}
