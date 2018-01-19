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

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uscool.io.event.R;
import uscool.io.event.data.Event;
import uscool.io.event.util.AppUtil;

/**
 * Created by andy1729 on 18/01/
 *
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
            Event event = mEventList.get(mEventList.size()-position-1);
            holder.tvUsername.setText(event.getUsername());
            if(event.getImgFilePath().equals("null")) {
                Glide.with(mContext).load(R.mipmap.img2).into(holder.ivFeedCenter);
            } else {
                Glide.with(mContext).load(event.getImgFilePath()).into(holder.ivFeedCenter);
            }
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
        TextView tvUsername;
        ImageButton btnComments;
        ImageButton btnLike;
        ImageButton btnMore;
        View vBgLike;
        ImageView ivLike;
        TextSwitcher tsLikesCounter;
        FrameLayout vImageRoot;


        private ViewHolder(View view) {
            super(view);
            ivFeedCenter = view.findViewById(R.id.ivFeedCenter);
            tvUsername = view.findViewById(R.id.username);
            tvDescription = view.findViewById(R.id.descriptionText);
            btnComments = view.findViewById(R.id.btnComments);
            btnLike = view.findViewById(R.id.btnLike);
            btnMore = view.findViewById(R.id.btnMore);
            vBgLike = view.findViewById(R.id.vBgLike);
            ivLike = view.findViewById(R.id.ivLike);
            tsLikesCounter = view.findViewById(R.id.tsLikesCounter);
            vImageRoot = view.findViewById(R.id.vImageRoot);

            btnLike.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id) {
                case R.id.btnLike:
                    btnLike.setImageResource( R.mipmap.ic_heart_red);
                    break;
            }
        }


    }



}

