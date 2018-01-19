package uscool.io.event.home;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import uscool.io.event.R;
import uscool.io.event.addeditevent.AddEditEventActivity;
import uscool.io.event.data.Event;
import uscool.io.event.util.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by andy1729 on 18/01/18.
 *
 * Display a grid of {@link Event}s. User can choose to view all, active or completed events.
 */
public class EventsFragment extends Fragment implements EventsContract.View {

    private EventsContract.Presenter mPresenter;

    private EventsAdapter mEventsAdapter;

    private View mNoEventsView;

    private ImageView mNoEventIcon;

    private TextView mNoEventMainView;

    private TextView mNoEventAddView;

    private LinearLayout mEventsView;

    private ShimmerRecyclerView mRecyclerView;



    public EventsFragment() {
        // Requires empty public constructor
    }

    public static EventsFragment newInstance() {
        return new EventsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEventsAdapter = new EventsAdapter(getContext(), new ArrayList<Event>(0), mItemListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void requestForPermission() {

            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    promptForPermissionsDialog(getString(R.string.error_request_permission), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    100);
                        }
                    });

                } else {

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            100);
                }
            }

    }

    private void promptForPermissionsDialog(String message, DialogInterface.OnClickListener onClickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setMessage(message)
                .setPositiveButton(getString(R.string.ok), onClickListener)
                .setNegativeButton(getString(R.string.cancel), null)
                .create()
                .show();

    }

    @Override
    public void setPresenter(@NonNull EventsContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        mPresenter.result(requestCode, resultCode);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_events, container, false);

        // Set up events view
//        ListView listView =  root.findViewById(R.id.events_list);
//        listView.setAdapter(mListAdapter);
        mRecyclerView = root.findViewById(R.id.recyclerView);
        mEventsView =  root.findViewById(R.id.eventsLL);

        // Set up  no events view
        mNoEventsView = root.findViewById(R.id.noEvents);
        mNoEventIcon =  root.findViewById(R.id.noEventsIcon);
        mNoEventMainView =  root.findViewById(R.id.noEventsMain);
        mNoEventAddView =  root.findViewById(R.id.noEventsAdd);
        mNoEventAddView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddEvent();
            }
        });

        // Set up floating action button
        FloatingActionButton fab =
                 getActivity().findViewById(R.id.fab_add_event);

        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addNewEvent();
            }
        });

        // Set up progress indicator
        final ScrollChildSwipeRefreshLayout swipeRefreshLayout =
                 root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );

        setUpRecyclerView();
        // Set the scrolling view in the custom SwipeRefreshLayout.
        swipeRefreshLayout.setScrollUpChild(mRecyclerView);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadEvents(false);
            }
        });

        setHasOptionsMenu(true);

        return root;
    }

    private void setUpRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(1, 20, true));
        mRecyclerView.showShimmerAdapter();
        mRecyclerView.setAdapter(mEventsAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                mPresenter.loadEvents(true);
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.events_fragment_menu, menu);
    }


    /**
     * Listener for clicks on events in the ListView.
     */
    EventItemListener mItemListener = new EventItemListener() {
        @Override
        public void onEventClick(Event clickedEvent) {
            mPresenter.openEventDetails(clickedEvent);
        }
    };

    @Override
    public void setLoadingIndicator(final boolean active) {

        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl = getView().findViewById(R.id.refresh_layout);
        if(active) {
            mRecyclerView.showShimmerAdapter();
        } else {
            mRecyclerView.hideShimmerAdapter();
        }

        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });
    }

    @Override
    public void showEvents(List<Event> events) {
        mEventsAdapter.replaceData(events);

        mEventsView.setVisibility(View.VISIBLE);
        mNoEventsView.setVisibility(View.GONE);
    }

    @Override
    public void showNoEvents() {
        showNoEventsViews(
                getResources().getString(R.string.no_events_all),
                R.drawable.ic_assignment_turned_in_24dp,
                false
        );
    }


    @Override
    public void showSuccessfullySavedMessage() {
        showMessage(getString(R.string.successfully_saved_event_message));
    }

    private void showNoEventsViews(String mainText, int iconRes, boolean showAddView) {
        mEventsView.setVisibility(View.GONE);
        mNoEventsView.setVisibility(View.VISIBLE);

        mNoEventMainView.setText(mainText);
        mNoEventIcon.setImageDrawable(getResources().getDrawable(iconRes));
        mNoEventAddView.setVisibility(showAddView ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showAddEvent() {
        Intent intent = new Intent(getContext(), AddEditEventActivity.class);
        startActivityForResult(intent, AddEditEventActivity.REQUEST_ADD_TASK);
    }

    @Override
    public void showEventDetailsUi(String eventId) {
        // in it's own Activity, since it makes more sense that way and it gives us the flexibility
        // to show some Intent stubbing.
//        Intent intent = new Intent(getContext(), EventDetailActivity.class);
//        intent.putExtra(EventDetailActivity.EXTRA_TASK_ID, eventId);
//        startActivity(intent);
    }

    @Override
    public void showLoadingEventsError() {
        showMessage(getString(R.string.loading_events_error));
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    /*private static class EventsAdapter extends BaseAdapter {

        private List<Event> mEvents;
        private EventItemListener mItemListener;

        public EventsAdapter(List<Event> events, EventItemListener itemListener) {
            setList(events);
            mItemListener = itemListener;
        }

        public void replaceData(List<Event> events) {
            setList(events);
            notifyDataSetChanged();
        }

        private void setList(List<Event> events) {
            mEvents = checkNotNull(events);
        }

        @Override
        public int getCount() {
            return mEvents.size();
        }

        @Override
        public Event getItem(int i) {
            return mEvents.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View rowView = view;
            if (rowView == null) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                rowView = inflater.inflate(R.layout.item_event, viewGroup, false);
            }

            final Event event = getItem(i);

            TextView titleTV = (TextView) rowView.findViewById(R.id.title);
            titleTV.setText(event.getTitleForList());

            CheckBox completeCB = (CheckBox) rowView.findViewById(R.id.complete);

            // Active/completed event UI
            completeCB.setChecked(event.isCompleted());
            if (event.isCompleted()) {
                rowView.setBackgroundDrawable(viewGroup.getContext()
                        .getResources().getDrawable(R.drawable.list_completed_touch_feedback));
            } else {
                rowView.setBackgroundDrawable(viewGroup.getContext()
                        .getResources().getDrawable(R.drawable.touch_feedback));
            }

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemListener.onEventClick(event);
                }
            });

            return rowView;
        }
    }
*/
    public interface EventItemListener {

        void onEventClick(Event clickedEvent);
    }

}
