package com.tuquyet.soundcloud.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.tuquyet.soundcloud.R;
import com.tuquyet.soundcloud.service.SoundCloundService;
import com.tuquyet.soundcloud.ui.adapter.FragmentAdapter;
import com.tuquyet.soundcloud.ui.fragment.PlaylistFragment;
import com.tuquyet.soundcloud.ui.fragment.TrackFragment;

public class MainActivity extends AppCompatActivity {
    public static final String API_KEY = "08f79801a998c381762ec5b15e4914d5";
    private SoundCloundService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.tuquyet.soundcloud.R.layout.activity_main);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        addPages(viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);
    }

    //Add pages
    private void addPages(ViewPager vp) {
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(new TrackFragment(), getString(R.string.tracks));
        adapter.addFragment(new PlaylistFragment(), getString(R.string.playlists));
        //Set Adapter
        vp.setAdapter(adapter);
    }
}
