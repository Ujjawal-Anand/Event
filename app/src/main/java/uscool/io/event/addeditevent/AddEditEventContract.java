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

        void initPhotoPicker();

        void showEventsList();

        void setDescription(String description);

        void setImage(String filepath);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void saveEvent(String username, String imageFilePath, String description);

        void populateEvent();

        boolean isDataMissing();
    }
}
