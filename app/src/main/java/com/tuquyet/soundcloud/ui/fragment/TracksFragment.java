package com.tuquyet.soundcloud.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tuquyet.soundcloud.R;
import com.tuquyet.soundcloud.data.model.TrackModel;
import com.tuquyet.soundcloud.service.ServiceGenerator;
import com.tuquyet.soundcloud.service.SoundCloundService;
import com.tuquyet.soundcloud.ui.adapter.TrackAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tuquyet.soundcloud.ui.activity.MainActivity.API_KEY;
import static com.tuquyet.soundcloud.ui.activity.MainActivity.USER_ID;

/**
 * Created by tuquyet on 22/05/2017.
 */
public class TracksFragment extends Fragment {

    private SoundCloundService mService;
    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_track, null);
        mService = ServiceGenerator.createService(SoundCloundService.class);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_track);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        getTracksOfUser();
        return view;
    }

    private void getTracksOfUser() {
        mService.getTracksOfUser(USER_ID, API_KEY)
                .enqueue(new Callback<ArrayList<TrackModel>>() {
                    @Override
                    public void onResponse(Call<ArrayList<TrackModel>> call,
                                           Response<ArrayList<TrackModel>> response) {
                        if (response != null) {
                            mRecyclerView.setAdapter(new TrackAdapter(getContext(), response.body
                                ()));
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<TrackModel>> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
