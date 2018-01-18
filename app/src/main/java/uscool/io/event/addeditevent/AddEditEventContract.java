package uscool.io.event.addeditevent;

import uscool.io.event.BasePresenter;
import uscool.io.event.BaseView;

/**
 * Created by andy1729 on 18/01/18.
 *
 * This specifies the contract between the view and the presenter.
 */
public interface AddEditEventContract {

    interface View extends BaseView<Presenter> {

        void showEmptyEventError();

        void showEventsList();

        void setTitle(String title);

        void setDescription(String description);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void saveEvent(String title, String description);

        void populateEvent();

        boolean isDataMissing();
    }
}
