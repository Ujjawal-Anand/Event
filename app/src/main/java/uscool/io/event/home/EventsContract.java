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

        void showLoadingEventsError();

        void showNoEvents();

        void requestForPermission();

        void showSuccessfullySavedMessage();

        boolean isActive();

    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void loadEvents(boolean forceUpdate);

        void addNewEvent();

        void openEventDetails(@NonNull Event requestedEvent);

    }
}
