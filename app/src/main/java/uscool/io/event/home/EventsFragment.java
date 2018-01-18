package uscool.io.event.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.PopupMenu;
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

import uscool.io.event.R;
import uscool.io.event.addeditevent.AddEditEventActivity;
import uscool.io.event.data.Event;
import uscool.io.event.eventdetail.EventDetailActivity;

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

    private EventsAdapter mListAdapter;

    private View mNoEventsView;

    private ImageView mNoEventIcon;

    private TextView mNoEventMainView;

    private TextView mNoEventAddView;

    private LinearLayout mEventsView;

    private TextView mFilteringLabelView;

    public EventsFragment() {
        // Requires empty public constructor
    }

    public static EventsFragment newInstance() {
        return new EventsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new EventsAdapter(new ArrayList<Event>(0), mItemListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
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
        ListView listView =  root.findViewById(R.id.events_list);
        listView.setAdapter(mListAdapter);
        mFilteringLabelView =  root.findViewById(R.id.filteringLabel);
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
        // Set the scrolling view in the custom SwipeRefreshLayout.
        swipeRefreshLayout.setScrollUpChild(listView);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadEvents(false);
            }
        });

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear:
                mPresenter.clearCompletedEvents();
                break;
            case R.id.menu_filter:
                showFilteringPopUpMenu();
                break;
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

    @Override
    public void showFilteringPopUpMenu() {
        PopupMenu popup = new PopupMenu(getContext(), getActivity().findViewById(R.id.menu_filter));
        popup.getMenuInflater().inflate(R.menu.filter_events, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.active:
                        mPresenter.setFiltering(EventsFilterType.ACTIVE_TASKS);
                        break;
                    case R.id.completed:
                        mPresenter.setFiltering(EventsFilterType.COMPLETED_TASKS);
                        break;
                    default:
                        mPresenter.setFiltering(EventsFilterType.ALL_TASKS);
                        break;
                }
                mPresenter.loadEvents(false);
                return true;
            }
        });

        popup.show();
    }

    /**
     * Listener for clicks on events in the ListView.
     */
    EventItemListener mItemListener = new EventItemListener() {
        @Override
        public void onEventClick(Event clickedEvent) {
            mPresenter.openEventDetails(clickedEvent);
        }

        @Override
        public void onCompleteEventClick(Event completedEvent) {
            mPresenter.completeEvent(completedEvent);
        }

        @Override
        public void onActivateEventClick(Event activatedEvent) {
            mPresenter.activateEvent(activatedEvent);
        }
    };

    @Override
    public void setLoadingIndicator(final boolean active) {

        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl =
                (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);

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
        mListAdapter.replaceData(events);

        mEventsView.setVisibility(View.VISIBLE);
        mNoEventsView.setVisibility(View.GONE);
    }

    @Override
    public void showNoActiveEvents() {
        showNoEventsViews(
                getResources().getString(R.string.no_events_active),
                R.drawable.ic_check_circle_24dp,
                false
        );
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
    public void showNoCompletedEvents() {
        showNoEventsViews(
                getResources().getString(R.string.no_events_completed),
                R.drawable.ic_verified_user_24dp,
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
    public void showActiveFilterLabel() {
        mFilteringLabelView.setText(getResources().getString(R.string.label_active));
    }

    @Override
    public void showCompletedFilterLabel() {
        mFilteringLabelView.setText(getResources().getString(R.string.label_completed));
    }

    @Override
    public void showAllFilterLabel() {
        mFilteringLabelView.setText(getResources().getString(R.string.label_all));
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
        Intent intent = new Intent(getContext(), EventDetailActivity.class);
        intent.putExtra(EventDetailActivity.EXTRA_TASK_ID, eventId);
        startActivity(intent);
    }

    @Override
    public void showEventMarkedComplete() {
        showMessage(getString(R.string.event_marked_complete));
    }

    @Override
    public void showEventMarkedActive() {
        showMessage(getString(R.string.event_marked_active));
    }

    @Override
    public void showCompletedEventsCleared() {
        showMessage(getString(R.string.completed_events_cleared));
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

    private static class EventsAdapter extends BaseAdapter {

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

            completeCB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!event.isCompleted()) {
                        mItemListener.onCompleteEventClick(event);
                    } else {
                        mItemListener.onActivateEventClick(event);
                    }
                }
            });

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemListener.onEventClick(event);
                }
            });

            return rowView;
        }
    }

    public interface EventItemListener {

        void onEventClick(Event clickedEvent);

        void onCompleteEventClick(Event completedEvent);

        void onActivateEventClick(Event activatedEvent);
    }

}
