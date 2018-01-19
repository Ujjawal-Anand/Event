package uscool.io.event.addeditevent;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import uscool.io.event.data.Event;
import uscool.io.event.data.source.EventsDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by andy1729 on 18/01/18.
 *
 * Listens to user actions from the UI ({@link AddEditEventFragment}), retrieves the data and updates
 * the UI as required.
 */
public class AddEditEventPresenter implements AddEditEventContract.Presenter,
        EventsDataSource.GetEventCallback {

    @NonNull
    private final EventsDataSource mEventsRepository;

    @NonNull
    private final AddEditEventContract.View mAddEventView;

    @Nullable
    private String mEventId;

    private boolean mIsDataMissing;

    /**
     * Creates a presenter for the add/edit view.
     *
     * @param eventId ID of the event to edit or null for a new event
     * @param eventsRepository a repository of data for events
     * @param addEventView the add/edit view
     * @param shouldLoadDataFromRepo whether data needs to be loaded or not (for config changes)
     */
    public AddEditEventPresenter(@Nullable String eventId, @NonNull EventsDataSource eventsRepository,
                                 @NonNull AddEditEventContract.View addEventView, boolean shouldLoadDataFromRepo) {
        mEventId = eventId;
        mEventsRepository = checkNotNull(eventsRepository);
        mAddEventView = checkNotNull(addEventView);
        mIsDataMissing = shouldLoadDataFromRepo;

        mAddEventView.setPresenter(this);
    }

    @Override
    public void start() {
        if (!isNewEvent() && mIsDataMissing) {
            mAddEventView.initPhotoPicker();
            populateEvent();
        }
    }

    @Override
    public void saveEvent(String username, String imageFilePath, String description) {
            createEvent(username, imageFilePath, description);
    }

    @Override
    public void populateEvent() {
        if (isNewEvent()) {
            throw new RuntimeException("populateEvent() was called but event is new.");
        }
        mEventsRepository.getEvent(mEventId, this);
    }

    @Override
    public void onEventLoaded(Event event) {
        // The view may not be able to handle UI updates anymore
        if (mAddEventView.isActive()) {
            mAddEventView.setImage(event.getImageFilePath());
            mAddEventView.setDescription(event.getDescription());
        }
        mIsDataMissing = false;
    }

    @Override
    public void onDataNotAvailable() {
        // The view may not be able to handle UI updates anymore
        if (mAddEventView.isActive()) {
            mAddEventView.showEmptyEventError();
        }
    }

    @Override
    public boolean isDataMissing() {
        return mIsDataMissing;
    }

    private boolean isNewEvent() {
        return mEventId == null;
    }

    private void createEvent(String username, String filePath, String description) {
        Event newEvent = new Event(username, filePath, description);
        if (newEvent.isEmpty()) {
            mAddEventView.showEmptyEventError();
        } else {
            mEventsRepository.saveEvent(newEvent);
            mAddEventView.showEventsList();
        }
    }

}
