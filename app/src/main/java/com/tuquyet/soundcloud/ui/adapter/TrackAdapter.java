package com.tuquyet.soundcloud.ui.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tuquyet.soundcloud.R;
import com.tuquyet.soundcloud.ui.activity.PlaySongActivity;
import com.tuquyet.soundcloud.ui.item.ItemClickListener;
import com.tuquyet.soundcloud.ui.item.TrackItem;

import java.util.List;

/**
 * Created by tuquyet on 22/05/2017.
 */
public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {
    private List<TrackItem> mTrackItems;

    public TrackAdapter(List<TrackItem> trackItems) {
        mTrackItems = trackItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_track, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TrackItem trackItem = mTrackItems.get(position);
        holder.bindData(trackItem);
    }

    @Override
    public int getItemCount() {
        return mTrackItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageTrackAvatar;
        private TextView mTextTrackTitle;
        private TextView mTextTrackUser;
        private TextView mTextTrackDate;
        private ImageView mImageTrackWaveform;
        private ItemClickListener mItemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            initViews();
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener item) {
            this.mItemClickListener = item;
        }

        private void initViews() {
            mImageTrackAvatar = (ImageView) itemView.findViewById(R.id.image_track_avatar);
            mTextTrackTitle = (TextView) itemView.findViewById(R.id.text_track_title);
            mTextTrackUser = (TextView) itemView.findViewById(R.id.text_track_user);
            mTextTrackDate = (TextView) itemView.findViewById(R.id.text_track_date);
            mImageTrackWaveform = (ImageView) itemView.findViewById(R.id.image_track_waveform);
        }

        public void bindData(TrackItem trackItem) {
            mImageTrackAvatar.setImageResource(trackItem.getTrackAvatar());
            mTextTrackTitle.setText(trackItem.getTrackTitle());
            mTextTrackUser.setText(trackItem.getTrackUser());
            mTextTrackDate.setText(trackItem.getTrackDate());
            mImageTrackWaveform.setImageResource(trackItem.getTrackWaveform());
        }

        @Override
        public void onClick(View v) {
            TrackItem item = mTrackItems.get(getAdapterPosition());
            //Do something when item is clicked
            Intent intent = new Intent(v.getContext(), PlaySongActivity.class);
            v.getContext().startActivity(intent);
        }
    }
}
