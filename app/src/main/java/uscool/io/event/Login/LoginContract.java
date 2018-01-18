package uscool.io.event.Login;

/**
 * Created by andy1729 on 17/01/18.
 */

public interface LoginContract {
    interface LoginView {
        void showProgress();

        void hideProgress();

        void setUsernameError();

        void setPasswordError();

        void navigateToHome();
    }

    interface LoginPresenter {
        void validateCredentials(String username, String password);

        void onDestroy();
    }
}
