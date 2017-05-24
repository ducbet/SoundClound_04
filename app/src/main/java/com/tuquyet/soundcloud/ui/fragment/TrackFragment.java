package com.tuquyet.soundcloud.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tuquyet.soundcloud.R;
import com.tuquyet.soundcloud.ui.adapter.TrackAdapter;
import com.tuquyet.soundcloud.ui.item.TrackItem;

import java.util.ArrayList;

/**
 * Created by tuquyet on 22/05/2017.
 */
public class TrackFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_track, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_track);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setAdapter(new TrackAdapter(getTracks()));
        return view;
    }

    private ArrayList<TrackItem> getTracks() {
        ArrayList<TrackItem> trackItems = new ArrayList<>();
        //Add track Item from API to show
        return trackItems;
    }
}
