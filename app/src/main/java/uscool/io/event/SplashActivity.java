package uscool.io.event;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import uscool.io.event.login.LoginActivity;

/**
 * Created by andy1729 on 19/01/18.
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(i);

        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
