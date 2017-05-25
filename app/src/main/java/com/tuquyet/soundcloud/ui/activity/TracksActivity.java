package com.tuquyet.soundcloud.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.tuquyet.soundcloud.R;
import com.tuquyet.soundcloud.data.model.TrackModel;
import com.tuquyet.soundcloud.service.ServiceGenerator;
import com.tuquyet.soundcloud.service.SoundCloundService;
import com.tuquyet.soundcloud.ui.adapter.TrackAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tuquyet.soundcloud.ui.activity.MainActivity.API_KEY;
import static com.tuquyet.soundcloud.ui.activity.MainActivity.exampleUserId;

public class TracksActivity extends AppCompatActivity {

    private RecyclerView mRecyclerTrackInPlaylist;
    private SoundCloundService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_in_playlist);
        mService = ServiceGenerator.createService(SoundCloundService.class);
        mRecyclerTrackInPlaylist = (RecyclerView) findViewById(R.id.recycler_track_in_playlist);
        mRecyclerTrackInPlaylist.setLayoutManager(new LinearLayoutManager(this));
        getTracksOfUser();
    }

    private void getTracksOfUser() {
        mService.getTracksOfUser(exampleUserId, API_KEY)
                .enqueue(new Callback<ArrayList<TrackModel>>() {
                    @Override
                    public void onResponse(Call<ArrayList<TrackModel>> call,
                                           Response<ArrayList<TrackModel>> response) {
                        if (response != null) {
                            mRecyclerTrackInPlaylist.setAdapter(new TrackAdapter(response.body()));
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<TrackModel>> call, Throwable t) {
                        Toast.makeText(TracksActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}