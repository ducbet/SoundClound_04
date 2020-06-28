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
import com.tuquyet.soundcloud.data.model.PlaylistModel;
import com.tuquyet.soundcloud.ui.activity.TracksActivity;

import java.io.Serializable;
import java.util.List;

import static com.tuquyet.soundcloud.ui.adapter.TrackAdapter.LIST_TRACKS;

/**
 * Created by tuquyet on 22/05/2017.
 */
public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {
    public static final String SELECTED_PLAYLIST = "SELECTED_PLAYLIST";
    public static final String BUNDLE_PLAYLIST = "BUNDLE_PLAYLIST";
    private List<PlaylistModel> mPlaylistItems;

    public PlaylistAdapter(List<PlaylistModel> playlistItems) {
        mPlaylistItems = playlistItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_playlist, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PlaylistModel playlistItem = mPlaylistItems.get(position);
        holder.bindData(playlistItem);
    }

    @Override
    public int getItemCount() {
        return mPlaylistItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mImagePlaylistAvatar;
        private TextView mTextPlaylistTitle;
        private TextView mTextPlaylistUser;
        private TextView mTextPlaylistDate;
        private TextView mTextPlaylistDescription;
        private Context mContext;
        private PlaylistModel mPlaylistItem;

        public ViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            initViews();
            itemView.setOnClickListener(this);
        }

        private void initViews() {
            mImagePlaylistAvatar = (ImageView) itemView.findViewById(R.id.image_playlist_avatar);
            mTextPlaylistTitle = (TextView) itemView.findViewById(R.id.text_playlist_title);
            mTextPlaylistUser = (TextView) itemView.findViewById(R.id.text_playlist_user);
            mTextPlaylistDate = (TextView) itemView.findViewById(R.id.text_playlist_date);
            mTextPlaylistDescription = (TextView) itemView.findViewById(R.id
                    .text_playlist_description);
        }

        public void bindData(PlaylistModel playlistItem) {
            if (playlistItem != null) {
                mPlaylistItem = playlistItem;
                loadAvatar(playlistItem);
                mTextPlaylistTitle.setText(playlistItem.getTitle());
                mTextPlaylistDate.setText(playlistItem.getCreatedAt());
                mTextPlaylistDescription.setText(playlistItem.getDescription());
            }
        }

        @Override
        public void onClick(View v) {
            PlaylistModel item = mPlaylistItems.get(getAdapterPosition());
            //Do something when item is clicked
            Bundle bundle = new Bundle();
            bundle.putSerializable(SELECTED_PLAYLIST, mPlaylistItem);
            Intent intent = new Intent(v.getContext(), TracksActivity.class);
            intent.putExtra(BUNDLE_PLAYLIST, bundle);
            v.getContext().startActivity(intent);
        }

        private void loadAvatar(PlaylistModel playlistItem) {
            Glide.with(mContext)
                    .load(playlistItem.getArtworkUrl())
                    .centerCrop()
                    .error(R.drawable.soundcloud)
                    .placeholder(R.drawable.soundcloud)
                    .into(mImagePlaylistAvatar);
        }
    }
}
