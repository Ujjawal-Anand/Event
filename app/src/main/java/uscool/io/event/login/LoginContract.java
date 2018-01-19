package uscool.io.event.login;

/**
 * Created by andy1729 on 17/01/18.
 */

public interface LoginContract {
    interface LoginView {
        void showProgress();

        void checkIsLoggedIn();

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
