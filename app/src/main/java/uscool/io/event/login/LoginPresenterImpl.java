package uscool.io.event.login;


public class LoginPresenterImpl implements LoginContract.LoginPresenter, LoginInteractor.OnLoginFinishedListener {

    private LoginContract.LoginView loginView;
    private LoginInteractor loginInteractor;

    public LoginPresenterImpl(LoginContract.LoginView loginView, LoginInteractorImpl loginInteractor) {
        this.loginView = loginView;
        this.loginInteractor = loginInteractor;
    }

    @Override public void validateCredentials(String username, String password) {
        if (loginView != null) {
            loginView.showProgress();
        }

        loginInteractor.login(username, password, this);
    }

    @Override public void onDestroy() {
        loginView = null;
    }

    @Override public void onUsernameError() {
        if (loginView != null) {
            loginView.setUsernameError();
            loginView.hideProgress();
        }
    }

    @Override public void onPasswordError() {
        if (loginView != null) {
            loginView.setPasswordError();
            loginView.hideProgress();
        }
    }

    @Override public void onSuccess() {
        if (loginView != null) {
            loginView.navigateToHome();
        }
    }
}
