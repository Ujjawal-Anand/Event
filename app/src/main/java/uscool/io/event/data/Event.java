package uscool.io.event.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

import java.util.UUID;

/**
 * Created by andy1729 on 18/01/18.
 *
 * Immutable model class for a Event.
 */
@Entity(tableName = "events")
public final class Event {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "entryid")
    private final String mId;

    @Nullable
    @ColumnInfo(name = "title")
    private final String mTitle;

    @Nullable
    @ColumnInfo(name = "description")
    private final String mDescription;

/*
    @Nullable
    @ColumnInfo(name="imageId")
    private final int mImageId;
*/

    @ColumnInfo(name = "completed")
    private final boolean mCompleted;

    /**
     * Use this constructor to create a new active Event.
     *
     * @param title       title of the event
     * @param description description of the event
     */
    @Ignore
    public Event(@Nullable String title, @Nullable String description) {
        this(title, description, UUID.randomUUID().toString(), false);
    }

    /**
     * Use this constructor to create an active Event if the Event already has an id (copy of another
     * Event).
     *
     * @param title       title of the event
     * @param description description of the event
     * @param id          id of the event
     */
    @Ignore
    public Event(@Nullable String title, @Nullable String description, @NonNull String id) {
        this(title, description, id, false);
    }

    /**
     * Use this constructor to create a new completed Event.
     *
     * @param title       title of the event
     * @param description description of the event
     * @param completed   true if the event is completed, false if it's active
     */
    @Ignore
    public Event(@Nullable String title, @Nullable String description, boolean completed) {
        this(title, description, UUID.randomUUID().toString(), completed);
    }

    /**
     * Use this constructor to specify a completed Event if the Event already has an id (copy of
     * another Event).
     *
     * @param title       title of the event
     * @param description description of the event
     * @param id          id of the event
     * @param completed   true if the event is completed, false if it's active
     */
    public Event(@Nullable String title, @Nullable String description,
                 @NonNull String id, boolean completed) {
        mId = id;
        mTitle = title;
        mDescription = description;
        mCompleted = completed;
    }

    @NonNull
    public String getId() {
        return mId;
    }

    @Nullable
    public String getTitle() {
        return mTitle;
    }

    @Nullable
    public String getTitleForList() {
        if (!Strings.isNullOrEmpty(mTitle)) {
            return mTitle;
        } else {
            return mDescription;
        }
    }

    @Nullable
    public String getDescription() {
        return mDescription;
    }

    public boolean isCompleted() {
        return mCompleted;
    }

    public boolean isActive() {
        return !mCompleted;
    }

    public boolean isEmpty() {
        return Strings.isNullOrEmpty(mTitle) &&
               Strings.isNullOrEmpty(mDescription);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equal(mId, event.mId) &&
               Objects.equal(mTitle, event.mTitle) &&
               Objects.equal(mDescription, event.mDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mId, mTitle, mDescription);
    }

    @Override
    public String toString() {
        return "Event with title " + mTitle;
    }
}
