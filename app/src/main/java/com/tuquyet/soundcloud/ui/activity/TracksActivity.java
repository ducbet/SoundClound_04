package com.tuquyet.soundcloud.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tuquyet.soundcloud.R;
import com.tuquyet.soundcloud.data.model.PlaylistModel;
import com.tuquyet.soundcloud.ui.adapter.TrackAdapter;

import static com.tuquyet.soundcloud.ui.adapter.PlaylistAdapter.BUNDLE_PLAYLIST;
import static com.tuquyet.soundcloud.ui.adapter.PlaylistAdapter.SELECTED_PLAYLIST;

public class TracksActivity extends AppCompatActivity {
    private Intent mIntent;
    private RecyclerView mRecyclerTrackInPlaylist;
    private PlaylistModel mPlaylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_in_playlist);
        getPlaylist();
        mRecyclerTrackInPlaylist = (RecyclerView) findViewById(R.id.recycler_track_in_playlist);
        mRecyclerTrackInPlaylist.setLayoutManager(new LinearLayoutManager(this));
        if (mPlaylist != null) {
            mRecyclerTrackInPlaylist.setAdapter(new TrackAdapter(this, mPlaylist
                .getListTracks()));
        } else {
            mRecyclerTrackInPlaylist.setAdapter(new TrackAdapter(this, null));
        }
    }

    private void getPlaylist() {
        mIntent = getIntent();
        if (mIntent != null) {
            Bundle bundle = mIntent.getBundleExtra(BUNDLE_PLAYLIST);
            mPlaylist = (PlaylistModel) bundle.getSerializable(SELECTED_PLAYLIST);
        }
    }
}
