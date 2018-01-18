package uscool.io.event.eventdetail;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import uscool.io.event.data.Event;
import uscool.io.event.data.source.EventsDataSource;
import uscool.io.event.data.source.EventsRepository;
import com.google.common.base.Strings;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by andy1729 on 18/01/18.
 *
 * Listens to user actions from the UI ({@link EventDetailFragment}), retrieves the data and updates
 * the UI as required.
 */
public class EventDetailPresenter implements EventDetailContract.Presenter {

    private final EventsRepository mEventsRepository;

    private final EventDetailContract.View mEventDetailView;

    @Nullable
    private String mEventId;

    public EventDetailPresenter(@Nullable String eventId,
                                @NonNull EventsRepository eventsRepository,
                                @NonNull EventDetailContract.View eventDetailView) {
        mEventId = eventId;
        mEventsRepository = checkNotNull(eventsRepository, "eventsRepository cannot be null!");
        mEventDetailView = checkNotNull(eventDetailView, "eventDetailView cannot be null!");

        mEventDetailView.setPresenter(this);
    }

    @Override
    public void start() {
        openEvent();
    }

    private void openEvent() {
        if (Strings.isNullOrEmpty(mEventId)) {
            mEventDetailView.showMissingEvent();
            return;
        }

        mEventDetailView.setLoadingIndicator(true);
        mEventsRepository.getEvent(mEventId, new EventsDataSource.GetEventCallback() {
            @Override
            public void onEventLoaded(Event event) {
                // The view may not be able to handle UI updates anymore
                if (!mEventDetailView.isActive()) {
                    return;
                }
                mEventDetailView.setLoadingIndicator(false);
                if (null == event) {
                    mEventDetailView.showMissingEvent();
                } else {
                    showEvent(event);
                }
            }

            @Override
            public void onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!mEventDetailView.isActive()) {
                    return;
                }
                mEventDetailView.showMissingEvent();
            }
        });
    }

    @Override
    public void editEvent() {
        if (Strings.isNullOrEmpty(mEventId)) {
            mEventDetailView.showMissingEvent();
            return;
        }
        mEventDetailView.showEditEvent(mEventId);
    }

    @Override
    public void deleteEvent() {
        if (Strings.isNullOrEmpty(mEventId)) {
            mEventDetailView.showMissingEvent();
            return;
        }
        mEventsRepository.deleteEvent(mEventId);
        mEventDetailView.showEventDeleted();
    }

    @Override
    public void completeEvent() {
        if (Strings.isNullOrEmpty(mEventId)) {
            mEventDetailView.showMissingEvent();
            return;
        }
        mEventsRepository.completeEvent(mEventId);
        mEventDetailView.showEventMarkedComplete();
    }

    @Override
    public void activateEvent() {
        if (Strings.isNullOrEmpty(mEventId)) {
            mEventDetailView.showMissingEvent();
            return;
        }
        mEventsRepository.activateEvent(mEventId);
        mEventDetailView.showEventMarkedActive();
    }

    private void showEvent(@NonNull Event event) {
        String title = event.getTitle();
        String description = event.getDescription();

        if (Strings.isNullOrEmpty(title)) {
            mEventDetailView.hideTitle();
        } else {
            mEventDetailView.showTitle(title);
        }

        if (Strings.isNullOrEmpty(description)) {
            mEventDetailView.hideDescription();
        } else {
            mEventDetailView.showDescription(description);
        }
        mEventDetailView.showCompletionStatus(event.isCompleted());
    }
}
