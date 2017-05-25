package com.tuquyet.soundcloud.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tuquyet.soundcloud.R;
import com.tuquyet.soundcloud.data.model.TrackModel;
import com.tuquyet.soundcloud.ui.activity.PlaySongActivity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tuquyet on 22/05/2017.
 */
public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {

    private static final String LIST_TRACKS = "LIST_TRACKS";
    private static final String SELECTED_TRACK = "SELECTED_TRACK";
    private List<TrackModel> mTrackItems;

    public TrackAdapter(List<TrackModel> trackItems) {
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
        TrackModel trackItem = mTrackItems.get(position);
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
        private Context mContext;

        public ViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            initViews();
            itemView.setOnClickListener(this);
        }

        private void initViews() {
            mImageTrackAvatar = (ImageView) itemView.findViewById(R.id.image_track_avatar);
            mTextTrackTitle = (TextView) itemView.findViewById(R.id.text_track_title);
            mTextTrackUser = (TextView) itemView.findViewById(R.id.text_track_user);
            mTextTrackDate = (TextView) itemView.findViewById(R.id.text_track_date);
            mImageTrackWaveform = (ImageView) itemView.findViewById(R.id.image_track_waveform);
        }

        public void bindData(TrackModel trackItem) {
            if (trackItem != null) {
                loadAvatar(trackItem);
                mTextTrackTitle.setText(trackItem.getTitle());
                mTextTrackDate.setText(trackItem.getCreatedAt());
            }
        }

        @Override
        public void onClick(View v) {
            //Do something when item is clicked
            Intent intent = new Intent(v.getContext(), PlaySongActivity.class);
            intent.putExtra(LIST_TRACKS, (Serializable) mTrackItems);
            intent.putExtra(SELECTED_TRACK, getAdapterPosition());
            v.getContext().startActivity(intent);
        }

        private void loadAvatar(TrackModel trackItem) {
            Glide.with(mContext)
                    .load(trackItem.getArtworkUrl())
                    .centerCrop()
                    .error(R.mipmap.ic_launcher)
                    .placeholder(R.mipmap.ic_launcher_round)
                    .into(mImageTrackAvatar);
        }
    }
}
