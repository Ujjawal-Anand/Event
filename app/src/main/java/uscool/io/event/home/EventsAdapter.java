package uscool.io.event.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uscool.io.event.R;
import uscool.io.event.data.Event;
import uscool.io.event.util.AppUtil;

/**
 * Created by andy1729 on 18/01/18.
 */

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder>{
        private List<Event> mEventList;
        private Context mContext;
        private EventsFragment.EventItemListener mItemListener;



    public EventsAdapter(Context context, List<Event> eventList, EventsFragment.EventItemListener itemListener) {
            this.mEventList = eventList;
            this.mContext = context;
            this.mItemListener = itemListener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            Event event = mEventList.get(position);
            holder.ivFeedCenter.setImageBitmap(AppUtil.getBitmapFromFilePath(event.getImageFilePath()));
            holder.tvDescription.setText(event.getDescription());
            holder.btnLike.setImageResource(R.mipmap.ic_heart_outline_grey);
        }

    public void replaceData(List<Event> events) {
        setList(events);
        notifyDataSetChanged();
    }

    private void setList(List<Event> events) {
        mEventList = events;
    }


    @Override
    public int getItemCount() {
        return mEventList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivFeedCenter;
        TextView tvDescription;
        ImageButton btnComments;
        ImageButton btnLike;
        ImageButton btnMore;
        View vBgLike;
        ImageView ivLike;
        TextSwitcher tsLikesCounter;
        ImageView ivUserProfile;
        FrameLayout vImageRoot;


        private ViewHolder(View view) {
            super(view);
            ivFeedCenter = view.findViewById(R.id.ivFeedCenter);
            tvDescription = view.findViewById(R.id.descriptionText);
            btnComments = view.findViewById(R.id.btnComments);
            btnLike = view.findViewById(R.id.btnLike);
            btnMore = view.findViewById(R.id.btnMore);
            vBgLike = view.findViewById(R.id.vBgLike);
            ivLike = view.findViewById(R.id.ivLike);
            tsLikesCounter = view.findViewById(R.id.tsLikesCounter);
            ivUserProfile = view.findViewById(R.id.ivUserProfile);
            vImageRoot = view.findViewById(R.id.vImageRoot);

        }

        @Override
        public void onClick(View view) {
//
        }
    }



}

