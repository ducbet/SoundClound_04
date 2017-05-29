package com.tuquyet.soundcloud.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import java.util.concurrent.TimeUnit;

/**
 * Created by tuquyet on 22/05/2017.
 */
public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {
    public static final String BUNDLE_LIST_TRACKS = "BUNDLE_LIST_TRACKS";
    public static final String LIST_TRACKS = "LIST_TRACKS";
    public static final String SELECTED_TRACK = "SELECTED_TRACK";
    private List<TrackModel> mTrackItems;
    private Context mContext;

    public TrackAdapter(Context context, List<TrackModel> trackItems) {
        mContext = context;
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
        private TextView mTextTrackDuration;
        private TextView mTextTrackPlayCount;
        private TextView mTextTrackFavoriteCount;
        private TextView mTextTrackCommentCount;
        private TextView mTextTrackDownloadCount;
        private ImageView mImageTrackWaveform;
        private Context mContext;
        private int mTrackDurationSecond;
        private int mTrackDurationMinute;
        private int mTrackDurationHour;

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
            mTextTrackDuration = (TextView) itemView.findViewById(R.id.text_track_duration);
            mTextTrackPlayCount = (TextView) itemView.findViewById(R.id.text_track_play_count);
            mTextTrackFavoriteCount =
                (TextView) itemView.findViewById(R.id.text_track_favorite_count);
            mTextTrackCommentCount =
                (TextView) itemView.findViewById(R.id.text_track_comment_count);
            mTextTrackDownloadCount =
                (TextView) itemView.findViewById(R.id.text_track_download_count);
            mImageTrackWaveform = (ImageView) itemView.findViewById(R.id.image_track_waveform);
        }

        public void bindData(TrackModel trackItem) {
            if (trackItem != null) {
                loadAvatar(trackItem);
                mTextTrackTitle.setText(trackItem.getTitle());
                mTextTrackUser.setText(trackItem.getUser().getUsername());
                loadImage(trackItem.getWaveformUrl(), mImageTrackWaveform);
                setDuration(trackItem);
                mTextTrackDuration.setText(mTrackDurationHour + " : " +
                    mTrackDurationMinute + " : " + mTrackDurationSecond);
                mTextTrackPlayCount.setText(String.valueOf(trackItem.getPlaybackCount()));
                mTextTrackFavoriteCount.setText(String.valueOf(trackItem.getFavoritingsCount()));
                mTextTrackCommentCount.setText(String.valueOf(trackItem.getCommentCount()));
                mTextTrackDownloadCount.setText(String.valueOf(trackItem.getDownloadCount()));
            }
        }

        @Override
        public void onClick(View v) {
            //Do something when item is clicked
            Bundle bundle = new Bundle();
            bundle.putInt(SELECTED_TRACK, getAdapterPosition());
            bundle.putSerializable(LIST_TRACKS, (Serializable) mTrackItems);
            Intent intent = new Intent(v.getContext(), PlaySongActivity.class);
            intent.putExtra(BUNDLE_LIST_TRACKS, bundle);
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

        private void loadImage(String url, ImageView imgTarget) {
            Glide.with(mContext)
                .load(url)
                .centerCrop()
                .error(R.drawable.soundcloud)
                .placeholder(R.mipmap.ic_launcher_round)
                .into(imgTarget);
        }
        private void setDuration(TrackModel trackItem) {
            int millis = trackItem.getDuration();
            mTrackDurationHour = (int) TimeUnit.MILLISECONDS.toHours(millis);
            millis -= TimeUnit.HOURS.toMillis(mTrackDurationHour);
            mTrackDurationMinute = (int) TimeUnit.MILLISECONDS.toMinutes(millis);
            millis -= TimeUnit.MINUTES.toMillis(mTrackDurationMinute);
            mTrackDurationSecond = (int) TimeUnit.MILLISECONDS.toSeconds(millis);
        }
    }


}
