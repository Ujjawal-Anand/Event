package uscool.io.event.home;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import uscool.io.event.R;
import uscool.io.event.util.ActivityUtils;
import uscool.io.event.util.Injection;

public class EventsActivity extends AppCompatActivity {

    private static final String CURRENT_FILTERING_KEY = "CURRENT_FILTERING_KEY";

    private DrawerLayout mDrawerLayout;

    private EventsPresenter mEventsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        // Set up the toolbar.
        Toolbar toolbar =  findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.new_accent_color));
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();

        // Set up the navigation drawer.
        mDrawerLayout =  findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.new_accent_color);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, 0, 0);
        actionBarDrawerToggle.syncState();


        NavigationView navigationView =  findViewById(R.id.nav_view);


        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        EventsFragment eventsFragment =
                (EventsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (eventsFragment == null) {
            // Create the fragment
            eventsFragment = EventsFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), eventsFragment, R.id.contentFrame);
        }

        // Create the presenter
        mEventsPresenter = new EventsPresenter(
                Injection.provideEventsRepository(getApplicationContext()), eventsFragment);

        // Load previously saved state, if available.
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        outState.putSerializable(CURRENT_FILTERING_KEY, mEventsPresenter.getFiltering());

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.list_navigation_menu_item:
                                // Do nothing, we're already on that screen
                                break;
                            default:
                                break;
                        }
                        // Close the navigation drawer when an item is selected.
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }


}
