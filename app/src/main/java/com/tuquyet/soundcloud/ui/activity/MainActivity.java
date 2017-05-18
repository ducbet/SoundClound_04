package com.tuquyet.soundcloud.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tuquyet.soundcloud.service.SoundCloundService;

public class MainActivity extends AppCompatActivity {

    public static final String API_KEY = "08f79801a998c381762ec5b15e4914d5";
    private SoundCloundService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.tuquyet.soundcloud.R.layout.activity_main);
    }

}
