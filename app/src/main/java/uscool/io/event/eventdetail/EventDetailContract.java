package uscool.io.event.eventdetail;

import uscool.io.event.BasePresenter;
import uscool.io.event.BaseView;

/**
 * Created by andy1729 on 18/01/18.
 *
 * This specifies the contract between the view and the presenter.
 */
public interface EventDetailContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showMissingEvent();

        void hideTitle();

        void showTitle(String title);

        void hideDescription();

        void showDescription(String description);

        void showCompletionStatus(boolean complete);

        void showEditEvent(String eventId);

        void showEventDeleted();

        void showEventMarkedComplete();

        void showEventMarkedActive();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void editEvent();

        void deleteEvent();

        void completeEvent();

        void activateEvent();
    }
}
