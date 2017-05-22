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
import com.tuquyet.soundcloud.ui.adapter.PlaylistAdapter;
import com.tuquyet.soundcloud.ui.item.PlaylistItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tuquyet on 22/05/2017.
 */
public class PlaylistFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_playlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setAdapter(new PlaylistAdapter(getPlaylists()));
        return view;
    }

    private List<PlaylistItem> getPlaylists() {
        List<PlaylistItem> playlistItems = new ArrayList<>();
        //Add playlist from API to show
        return playlistItems;
    }
}
