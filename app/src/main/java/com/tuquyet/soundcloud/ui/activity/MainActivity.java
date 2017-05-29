package com.tuquyet.soundcloud.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tuquyet.soundcloud.R;
import com.tuquyet.soundcloud.data.model.UserModel;
import com.tuquyet.soundcloud.service.ServiceGenerator;
import com.tuquyet.soundcloud.service.SoundCloundService;
import com.tuquyet.soundcloud.ui.adapter.FragmentAdapter;
import com.tuquyet.soundcloud.ui.fragment.FollowingsFragment;
import com.tuquyet.soundcloud.ui.fragment.InfoFragment;
import com.tuquyet.soundcloud.ui.fragment.PlaylistsFragment;
import com.tuquyet.soundcloud.ui.fragment.TracksFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public static final String API_KEY = "64a52bb31abd2ec73f8adda86358cfbf";
    private SoundCloundService mService;
    public static final int USER_ID = 38247973;
    private UserModel mUserModel;
    private ImageView mImageMyAvatar;
    private TextView mTextMyUserName;
    private TextView mTextMyFullName;
    private TextView mTextMyCity;
    private TextView mTextMyCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.tuquyet.soundcloud.R.layout.activity_main);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        addPages(viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);
        mService = ServiceGenerator.createService(SoundCloundService.class);
        initView();
        getUser();
    }

    private void initView() {
        mImageMyAvatar = (ImageView) findViewById(R.id.image_my_avatar);
        mTextMyUserName = (TextView) findViewById(R.id.text_my_user_name);
        mTextMyFullName = (TextView) findViewById(R.id.text_my_full_name);
        mTextMyCity = (TextView) findViewById(R.id.text_my_city);
        mTextMyCountry = (TextView) findViewById(R.id.text_my_country);
    }

    //Add pages
    private void addPages(ViewPager vp) {
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(new InfoFragment(), getString(R.string.user_info));
        adapter.addFragment(new TracksFragment(), getString(R.string.tracks));
        adapter.addFragment(new PlaylistsFragment(), getString(R.string.playlists));
        adapter.addFragment(new FollowingsFragment(), getString(R.string.following));
        //Set Adapter
        vp.setAdapter(adapter);
    }

    private void loadImage(String url, ImageView imgTarget) {
        Glide.with(MainActivity.this)
            .load(url)
            .centerCrop()
            .error(R.drawable.soundcloud)
            .placeholder(R.mipmap.ic_launcher_round)
            .into(imgTarget);
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
                    loadImage(mUserModel.getAvatarUrl(), mImageMyAvatar);
                    mTextMyUserName.setText(mUserModel.getUsername());
                    mTextMyFullName.setText(mUserModel.getFullName());
                    mTextMyCity.setText(mUserModel.getCity() + ",");
                    mTextMyCountry.setText(mUserModel.getCountry());
                }

                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {
                    Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT)
                        .show();
                }
            });
    }
}
