package uscool.io.event.home;

import android.support.annotation.NonNull;

import uscool.io.event.BaseView;
import uscool.io.event.data.Event;
import uscool.io.event.BasePresenter;

import java.util.List;

/**
 * Created by andy1729 on 18/01/18.
 *
 * This specifies the contract between the view and the presenter.
 */
public interface EventsContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showEvents(List<Event> events);

        void showAddEvent();

        void showEventDetailsUi(String eventId);

        void showEventMarkedComplete();

        void showEventMarkedActive();

        void showCompletedEventsCleared();

        void showLoadingEventsError();

        void showNoEvents();

        void showActiveFilterLabel();

        void showCompletedFilterLabel();

        void showAllFilterLabel();

        void showNoActiveEvents();

        void showNoCompletedEvents();

        void showSuccessfullySavedMessage();

        boolean isActive();

        void showFilteringPopUpMenu();
    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void loadEvents(boolean forceUpdate);

        void addNewEvent();

        void openEventDetails(@NonNull Event requestedEvent);

        void completeEvent(@NonNull Event completedEvent);

        void activateEvent(@NonNull Event activeEvent);

        void clearCompletedEvents();

        void setFiltering(EventsFilterType requestType);

        EventsFilterType getFiltering();
    }
}
