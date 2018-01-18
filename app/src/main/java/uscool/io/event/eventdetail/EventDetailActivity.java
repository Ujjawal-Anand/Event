package uscool.io.event.eventdetail;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import uscool.io.event.R;
import uscool.io.event.util.ActivityUtils;
import uscool.io.event.util.Injection;

/**
 * Created by andy1729 on 18/01/18.
 *
 * Displays event details screen.
 */
public class EventDetailActivity extends AppCompatActivity {

    public static final String EXTRA_TASK_ID = "TASK_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event_detail);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        // Get the requested event id
        String eventId = getIntent().getStringExtra(EXTRA_TASK_ID);

        EventDetailFragment eventDetailFragment = (EventDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (eventDetailFragment == null) {
            eventDetailFragment = EventDetailFragment.newInstance(eventId);

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    eventDetailFragment, R.id.contentFrame);
        }

        // Create the presenter
        new EventDetailPresenter(
                eventId,
                Injection.provideEventsRepository(getApplicationContext()),
                eventDetailFragment);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
