package com.tuquyet.soundcloud.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tuquyet.soundcloud.R;
import com.tuquyet.soundcloud.data.model.ObjectListUser;
import com.tuquyet.soundcloud.service.ServiceGenerator;
import com.tuquyet.soundcloud.service.SoundCloundService;
import com.tuquyet.soundcloud.ui.adapter.UserAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tuquyet.soundcloud.ui.activity.MainActivity.API_KEY;
import static com.tuquyet.soundcloud.ui.activity.MainActivity.exampleUserId;

/**
 * Created by tuquyet on 23/05/2017.
 */
public class FollowingsFragment extends Fragment {

    private SoundCloundService mService;
    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, null);
        mService = ServiceGenerator.createService(SoundCloundService.class);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_user);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        getFollowingsOfUser();
        return view;
    }

    private void getFollowingsOfUser() {
        mService.getFollowingsOfUser(exampleUserId, API_KEY)
                .enqueue(new Callback<ObjectListUser>() {
                    @Override
                    public void onResponse(Call<ObjectListUser> call,
                                           Response<ObjectListUser> response) {
                        if (response != null) {
                            mRecyclerView.setAdapter(new UserAdapter(response.body().getListUsers()));
                        }
                    }

                    @Override
                    public void onFailure(Call<ObjectListUser> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }
}
