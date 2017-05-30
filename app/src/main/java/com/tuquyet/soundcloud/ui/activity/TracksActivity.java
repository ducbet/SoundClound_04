package com.tuquyet.soundcloud.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

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
        removeTittleBar();
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
            ((TextView) findViewById(R.id.text_view_playlist_title)).setText(mPlaylist.getTitle());
        }
    }

    public void removeTittleBar() {
        //remove title bar,..//phai de truoc setContentView(R.layout.activity_animation);
        getSupportActionBar().hide();//hide Title Bar (tên application)
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION //hide Navigation Bar (thanh dưới cùng)
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY; // quay trở lại trạng thái ẩn nếu không chạm nữa
        decorView.setSystemUiVisibility(uiOptions);
    }
}
