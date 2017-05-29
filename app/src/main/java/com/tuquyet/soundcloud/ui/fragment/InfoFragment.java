package com.tuquyet.soundcloud.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tuquyet.soundcloud.R;
import com.tuquyet.soundcloud.data.model.UserModel;
import com.tuquyet.soundcloud.service.ServiceGenerator;
import com.tuquyet.soundcloud.service.SoundCloundService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tuquyet.soundcloud.ui.activity.MainActivity.API_KEY;
import static com.tuquyet.soundcloud.ui.activity.MainActivity.USER_ID;

/**
 * Created by tuquyet on 22/05/2017.
 */
public class InfoFragment extends Fragment {
    private SoundCloundService mService;
    private UserModel mUserModel;
    private TextView mInfoUserName;
    private TextView mInfoFullName;
    private TextView mInfoDescription;
    private TextView mInfoCountry;
    private TextView mInfoCity;
    private TextView mInfoTracks;
    private TextView mInfoPlaylists;
    private TextView mInfoFollower;
    private TextView mInfoFollowing;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, null);
        mService = ServiceGenerator.createService(SoundCloundService.class);
        initView(view);
        getUser();
        return view;
    }

    private void initView(View v) {
        mInfoUserName = (TextView) v.findViewById(R.id.text_info_username);
        mInfoFullName = (TextView) v.findViewById(R.id.text_info_fullname);
        mInfoDescription = (TextView) v.findViewById(R.id.text_info_description);
        mInfoCountry = (TextView) v.findViewById(R.id.text_info_country);
        mInfoCity = (TextView) v.findViewById(R.id.text_info_city);
        mInfoTracks = (TextView) v.findViewById(R.id.text_info_tracks);
        mInfoPlaylists = (TextView) v.findViewById(R.id.text_info_playlists);
        mInfoFollower = (TextView) v.findViewById(R.id.text_info_followers);
        mInfoFollowing = (TextView) v.findViewById(R.id.text_info_following);
    }

    private void getUser() {
        mService.getUser(USER_ID, API_KEY)
            .enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call,
                                       Response<UserModel> response) {
                    if (response != null) {
                        mUserModel = response.body();
                        loadData();
                    }
                }

                private void loadData() {
                    mInfoUserName.setText(mUserModel.getUsername());
                    mInfoFullName.setText(mUserModel.getFullName());
                    mInfoDescription.setText(mUserModel.getDescription());
                    mInfoCountry.setText(mUserModel.getCountry());
                    mInfoCity.setText(mUserModel.getCity());
                    mInfoTracks.setText(String.valueOf(mUserModel.getTrackCount()));
                    mInfoPlaylists.setText(String.valueOf(mUserModel.getPlaylistCount()));
                    mInfoFollower.setText(String.valueOf(mUserModel.getFollowersCount()));
                    mInfoFollowing.setText(String.valueOf(mUserModel.getFollowingsCount()));
                }

                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT)
                        .show();
                }
            });
    }
}
