package uscool.io.event.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import uscool.io.event.R;
import uscool.io.event.home.EventsActivity;
import uscool.io.event.util.PrefUtil;


public class LoginActivity extends Activity implements LoginContract.LoginView, View.OnClickListener {

    private ProgressBar progressBar;
    private EditText username;
    private EditText password;
    private EditText email;
    private LoginContract.LoginPresenter presenter;
    private ProgressDialog mProgressDialog;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressBar =  findViewById(R.id.progress);
        username =  findViewById(R.id.username);
        email = findViewById(R.id.email);
        password =  findViewById(R.id.password);
        findViewById(R.id.signUpButton).setOnClickListener(this);

        presenter = new LoginPresenterImpl(this,new LoginInteractorImpl());
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkIsLoggedIn();
    }


    @Override
    public void checkIsLoggedIn() {
        if(PrefUtil.isLoggedIn(getApplicationContext())) {
            navigateToHome();
        }
    }

    @Override protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override public void showProgress() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
        }

        mProgressDialog.show();
    }

    @Override public void hideProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override public void setUsernameError() {
        username.setError(getString(R.string.username_error));
    }

    @Override public void setPasswordError() {
        password.setError(getString(R.string.password_error));
    }

    @Override public void navigateToHome() {
        startActivity(new Intent(this, EventsActivity.class));
        finish();
    }

    @Override public void onClick(View v) {
        presenter.validateCredentials(username.getText().toString(), password.getText().toString());
        PrefUtil.createUser(getApplicationContext(), username.getText().toString(), password.getText().toString());
    }
}
