package com.tuquyet.soundcloud.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.tuquyet.soundcloud.R;
import com.tuquyet.soundcloud.data.model.TrackModel;

import static com.tuquyet.soundcloud.service.TrackReceiver.ACTION_PAUSE;
import static com.tuquyet.soundcloud.service.TrackReceiver.ACTION_PLAY;
import static com.tuquyet.soundcloud.service.TrackReceiver.ACTION_RETURN_SONG_STATUS;
import static com.tuquyet.soundcloud.service.TrackReceiver.ACTION_RETURN_TRACK;
import static com.tuquyet.soundcloud.service.TrackReceiver.ACTION_UPDATE_PROGRESSBAR;
import static com.tuquyet.soundcloud.util.widget.NavigationSongBar.SEEK_BAR_MAX;

/**
 * Created by tmd on 27/04/2017.
 */
public class PlayBackGroundService extends Service {
    private static final String TAG = "MY_SampleService";
    private static final int NOTIFICATION_ID = 1;
    public static final String ACTION_BACK = "com.example.tmd.service.ACTION_BACK";
    public static final String ACTION_PLAY_PAUSE = "com.example.tmd.service.ACTION_PLAY_PAUSE";
    public static final String ACTION_NEXT = "com.example.tmd.service.ACTION_NEXT";
    public static final String ACTION_SELECT_SONG = "com.example.tmd.service.ACTION_SELECT_SONG";
    public static final String ACTION_SEEK = "com.example.tmd.service.ACTION_SEEK";
    public static final String ACTION_GET_SONG_STATUS =
            "com.example.tmd.service.ACTION_GET_SONG_STATUS";
    public static final String EXTRA_SONG_ID = "com.example.tmd.service.EXTRA_SONG_ID";
    public static final String EXTRA_RETURN_TRACK = "com.example.tmd.service.EXTRA_RETURN_TRACK";
    public static final String EXTRA_SEEK = "com.example.tmd.service.EXTRA_SEEK";
    public static final String EXTRA_TRACK = "com.example.tmd.service.EXTRA_TRACK";
    public static final String EXTRA_SONG_STATUS = "com.example.tmd.service.EXTRA_SONG_STATUS";
    private Handler mHandler;
    private MediaPlayer mMediaPlayer;
    private TrackModel mTrackModel;
    private Intent mIntent;
    private int mTimeDelay;
    private boolean mIsUpdatingProgressBar;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Notification notification = null;
        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        builder.setContentText("Soundcloud")
                .setSmallIcon(R.drawable.soundcloud);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notification = builder.build();
        }
        startForeground(NOTIFICATION_ID, notification);
        mMediaPlayer = new MediaPlayer();
        mHandler = new Handler();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) return START_STICKY;
        String action = intent.getAction();
        if (action == null) return START_STICKY;
        switch (action) {
            case ACTION_SELECT_SONG:
                mTrackModel = (TrackModel) intent.getSerializableExtra(EXTRA_TRACK);
                int songID = intent.getIntExtra(EXTRA_SONG_ID, 0);
                if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                }
                mMediaPlayer = MediaPlayer.create(getApplicationContext(), songID);
                mMediaPlayer.start();
                sendBroadcast(ACTION_PLAY);

                calculateDelayTime();
                if (!mIsUpdatingProgressBar) {
                    mIsUpdatingProgressBar = true;
                    updateProgressBar();
                }
                mIntent = new Intent();
                mIntent.setAction(ACTION_RETURN_TRACK);
                mIntent.putExtra(EXTRA_RETURN_TRACK, mTrackModel);
                sendBroadcast(mIntent);
                break;
            case ACTION_BACK:
                break;
            case ACTION_PLAY_PAUSE:
                if (mMediaPlayer != null) {
                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.pause();
                        sendBroadcast(ACTION_PAUSE);
                    } else {
                        mMediaPlayer.start();
                        if (!mIsUpdatingProgressBar) {
                            mIsUpdatingProgressBar = true;
                            updateProgressBar();
                        }
                        sendBroadcast(ACTION_PLAY);
                    }
                }
                break;
            case ACTION_NEXT:
                break;
            case ACTION_SEEK:
                int seekPercent = intent.getIntExtra(EXTRA_SEEK, 0);
                int seekMili = convertPercentToMili(seekPercent);
                mMediaPlayer.seekTo(seekMili);
                mMediaPlayer.start();
                sendBroadcast(ACTION_PLAY);
                if (!mIsUpdatingProgressBar) {
                    mIsUpdatingProgressBar = true;
                    updateProgressBar();
                }
                break;
            case ACTION_GET_SONG_STATUS:
                mIntent = new Intent();
                mIntent.setAction(ACTION_RETURN_SONG_STATUS);
                mIntent.putExtra(EXTRA_SONG_STATUS, mMediaPlayer.isPlaying());
                sendBroadcast(mIntent);
                break;
            default:
                break;
        }
        return START_STICKY;
    }

    private void sendBroadcast(String action) {
        mIntent = new Intent();
        mIntent.setAction(action);
        sendBroadcast(mIntent);
    }

    private void calculateDelayTime() {
        int duration = mMediaPlayer.getDuration();
        mTimeDelay = duration / SEEK_BAR_MAX;
        if (mTimeDelay == 0) mTimeDelay = 1;
    }

    private void updateProgressBar() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mMediaPlayer != null) {
                    Intent intentUpdateProgressBar = new Intent();
                    if (mMediaPlayer.isPlaying()) {
                        intentUpdateProgressBar.setAction(ACTION_UPDATE_PROGRESSBAR);
                        intentUpdateProgressBar.putExtra(ACTION_UPDATE_PROGRESSBAR,
                                convertMiliToPercent(mMediaPlayer.getCurrentPosition()));
                        sendBroadcast(intentUpdateProgressBar);
                        updateProgressBar();
                    } else {
                        mIsUpdatingProgressBar = false;
                        intentUpdateProgressBar.setAction(ACTION_PAUSE);
                        sendBroadcast(intentUpdateProgressBar);
                    }
                }
            }
        }, mTimeDelay);
    }

    private int convertPercentToMili(int seekPercent) {
        int duration = mMediaPlayer.getDuration();
        int mili = duration * seekPercent / SEEK_BAR_MAX;
        return mili;
    }

    private int convertMiliToPercent(int seekMili) {
        int duration = mMediaPlayer.getDuration();
        int percent = seekMili * SEEK_BAR_MAX / duration;
        return percent;
    }
}
