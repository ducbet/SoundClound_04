package com.tuquyet.soundcloud.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tuquyet.soundcloud.R;
import com.tuquyet.soundcloud.ui.item.ItemClickListener;
import com.tuquyet.soundcloud.ui.item.PlaylistItem;


import java.util.List;

/**
 * Created by tuquyet on 22/05/2017.
 */
public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {
    private List<PlaylistItem> mPlaylistItems;

    public PlaylistAdapter(List<PlaylistItem> playlistItems) {
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
        PlaylistItem playlistItem = mPlaylistItems.get(position);
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
            mImagePlaylistAvatar = (ImageView) itemView.findViewById(R.id.image_playlist_avatar);
            mTextPlaylistTitle = (TextView) itemView.findViewById(R.id.text_playlist_title);
            mTextPlaylistUser = (TextView) itemView.findViewById(R.id.text_playlist_user);
            mTextPlaylistDate = (TextView) itemView.findViewById(R.id.text_playlist_date);
            mTextPlaylistDescription = (TextView) itemView.findViewById(R.id
                .text_playlist_description);
        }

        public void bindData(PlaylistItem playlistItem) {
            if (mPlaylistItems.size() != 0) {
                mImagePlaylistAvatar.setImageResource(playlistItem.getPlaylistAvatar());
                mTextPlaylistTitle.setText(playlistItem.getPlaylistTitle());
                mTextPlaylistUser.setText(playlistItem.getPlaylistUser());
                mTextPlaylistDate.setText(playlistItem.getPlaylistDate());
                mTextPlaylistDescription.setText(playlistItem.getPlaylistDescription());
            }
        }

        @Override
        public void onClick(View v) {
            PlaylistItem item = mPlaylistItems.get(getAdapterPosition());
            //Do something when item is clicked
            Toast.makeText(v.getContext(), item.getPlaylistTitle(), Toast.LENGTH_SHORT).show();
        }
    }
}
