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
    @ColumnInfo(name="username")
    private final String mUsername;

    @Nullable
    @ColumnInfo(name = "imageFilePath")
    private final String mImgFilePath;

    @Nullable
    @ColumnInfo(name = "description")
    private final String mDescription;

    @ColumnInfo(name="likes")
    private final int mLikes;


    /**
     * Use this constructor to create a new active Event.
     *
     * @param username username
     * @param imgFilePath       imgFilePath of the event
     * @param description description of the event
     */
    @Ignore
    public Event(@Nullable String username, @Nullable String imgFilePath, @Nullable String description) {
        this(username, imgFilePath, description, UUID.randomUUID().toString(), 0);
    }

    /**
     * Use this constructor to specify a completed Event if the Event already has an id (copy of
     * another Event).
     *
     * @param username username
     * @param imgFilePath       imgFilePath of the event
     * @param description description of the event
     * @param id          id of the event
     * @param likes   num of likes
     */
    public Event(@Nullable String username, @Nullable String imgFilePath, @Nullable String description,
                 @NonNull String id, int likes) {
        mId = id;
        mUsername = username;
        mImgFilePath = imgFilePath;
        mDescription = description;
        mLikes = likes;
    }

    @NonNull
    public String getId() {
        return mId;
    }

    @Nullable
    public String getImgFilePath() {
        return mImgFilePath;
    }

    @Nullable
    public int getLikes() {
        return mLikes;
    }

    @Nullable
    public String getUsername() {
        return mUsername;
    }

    @Nullable
    public String getDescription() {
        return mDescription;
    }

    public boolean isEmpty() {
        return Strings.isNullOrEmpty(mImgFilePath) &&
               Strings.isNullOrEmpty(mDescription);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equal(mId, event.mId) &&
                Objects.equal(mUsername, event.mUsername) &&
               Objects.equal(mImgFilePath, event.mImgFilePath) &&
               Objects.equal(mDescription, event.mDescription) &&
                Objects.equal(mLikes, event.mLikes);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mId,mUsername, mImgFilePath, mDescription);
    }

    @Override
    public String toString() {
        return "Event with title " + mImgFilePath;
    }
}
