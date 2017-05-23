package com.tuquyet.soundcloud.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tuquyet.soundcloud.R;

/**
 * Created by tuquyet on 22/05/2017.
 */
public class InfoFragment extends Fragment {
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
        initView(view);
        setText();
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

    private void setText() {
        //Set info from API
        mInfoUserName.setText("");
        mInfoFullName.setText("");
        mInfoDescription.setText("");
        mInfoCountry.setText("");
        mInfoCity.setText("");
        mInfoTracks.setText("");
        mInfoPlaylists.setText("");
        mInfoFollower.setText("");
        mInfoFollowing.setText("");
    }
}
