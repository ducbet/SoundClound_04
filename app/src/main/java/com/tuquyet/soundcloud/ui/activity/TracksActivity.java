package com.tuquyet.soundcloud.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tuquyet.soundcloud.R;
import com.tuquyet.soundcloud.ui.adapter.TrackAdapter;
import com.tuquyet.soundcloud.ui.item.TrackItem;

import java.util.ArrayList;
import java.util.List;

public class TracksActivity extends AppCompatActivity {
    private RecyclerView mRecyclerTrackInPlaylist;
    private TrackAdapter mTrackAdapter;
    private List<TrackItem> mTrackItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_in_playlist);
        mRecyclerTrackInPlaylist = (RecyclerView) findViewById(R.id.recycler_track_in_playlist);
        mRecyclerTrackInPlaylist.setLayoutManager(new LinearLayoutManager(this));
        mTrackItemList = new ArrayList<TrackItem>();
        mTrackAdapter = new TrackAdapter(mTrackItemList);
        mRecyclerTrackInPlaylist.setAdapter(mTrackAdapter);
    }
}
