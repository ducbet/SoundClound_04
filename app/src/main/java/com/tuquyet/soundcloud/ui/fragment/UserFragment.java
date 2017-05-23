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
import com.tuquyet.soundcloud.ui.adapter.UserAdapter;
import com.tuquyet.soundcloud.ui.item.UserItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tuquyet on 23/05/2017.
 */
public class UserFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_user);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setAdapter(new UserAdapter(getUsers()));
        return view;
    }

    private List<UserItem> getUsers() {
        List<UserItem> userItems = new ArrayList<>();
        //Add user from API to show
        return userItems;
    }
}
