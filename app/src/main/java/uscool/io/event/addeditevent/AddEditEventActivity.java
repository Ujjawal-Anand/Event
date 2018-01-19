package uscool.io.event.addeditevent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import uscool.io.event.R;
import uscool.io.event.photoPicker.DefaultCallback;
import uscool.io.event.photoPicker.EasyImage;
import uscool.io.event.util.ActivityUtils;
import uscool.io.event.util.Injection;

/**
 * Created by andy1729 on 18/01/18.
 *
 * Displays an add or edit event screen.
 */
public class AddEditEventActivity extends AppCompatActivity {

    public static final int REQUEST_ADD_TASK = 1;

    public static final String SHOULD_LOAD_DATA_FROM_REPO_KEY = "SHOULD_LOAD_DATA_FROM_REPO_KEY";

    private AddEditEventPresenter mAddEditEventPresenter;

    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(true);

        AddEditEventFragment addEditEventFragment = (AddEditEventFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        String eventId = getIntent().getStringExtra(AddEditEventFragment.ARGUMENT_EDIT_TASK_ID);

        setToolbarTitle(eventId);

        if (addEditEventFragment == null) {
            addEditEventFragment = AddEditEventFragment.newInstance();

            if (getIntent().hasExtra(AddEditEventFragment.ARGUMENT_EDIT_TASK_ID)) {
                Bundle bundle = new Bundle();
                bundle.putString(AddEditEventFragment.ARGUMENT_EDIT_TASK_ID, eventId);
                addEditEventFragment.setArguments(bundle);
            }

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    addEditEventFragment, R.id.contentFrame);
        }

        boolean shouldLoadDataFromRepo = true;

        // Prevent the presenter from loading data from the repository if this is a config change.
        if (savedInstanceState != null) {
            // Data might not have loaded when the config change happen, so we saved the state.
            shouldLoadDataFromRepo = savedInstanceState.getBoolean(SHOULD_LOAD_DATA_FROM_REPO_KEY);
        }

        // Create the presenter
        mAddEditEventPresenter = new AddEditEventPresenter(
                eventId,
                Injection.provideEventsRepository(getApplicationContext()),
                addEditEventFragment,
                shouldLoadDataFromRepo);
    }

    private void setToolbarTitle(@Nullable String eventId) {
        if(eventId == null) {
            mActionBar.setTitle(R.string.add_event);
        } else {
            mActionBar.setTitle(R.string.edit_event);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save the state so that next time we know if we need to refresh data.
        outState.putBoolean(SHOULD_LOAD_DATA_FROM_REPO_KEY, mAddEditEventPresenter.isDataMissing());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }




}
