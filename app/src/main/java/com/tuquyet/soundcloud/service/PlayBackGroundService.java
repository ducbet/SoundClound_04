package com.tuquyet.soundcloud.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.tuquyet.soundcloud.data.model.TrackModel;
import com.tuquyet.soundcloud.util.widget.NotificationSong;

import java.util.List;

import static com.tuquyet.soundcloud.service.TrackReceiver.ACTION_PAUSE;
import static com.tuquyet.soundcloud.service.TrackReceiver.ACTION_PLAY;
import static com.tuquyet.soundcloud.service.TrackReceiver.ACTION_RETURN_SONG_STATUS;
import static com.tuquyet.soundcloud.service.TrackReceiver.ACTION_RETURN_TRACK;
import static com.tuquyet.soundcloud.service.TrackReceiver.ACTION_UPDATE_PROGRESSBAR;
import static com.tuquyet.soundcloud.ui.activity.MainActivity.API_KEY;
import static com.tuquyet.soundcloud.ui.adapter.TrackAdapter.BUNDLE_LIST_TRACKS;
import static com.tuquyet.soundcloud.ui.adapter.TrackAdapter.LIST_TRACKS;
import static com.tuquyet.soundcloud.ui.adapter.TrackAdapter.SELECTED_TRACK;
import static com.tuquyet.soundcloud.util.widget.NavigationSongBar.SEEK_BAR_MAX;

/**
 * Created by tmd on 27/04/2017.
 */
public class PlayBackGroundService extends Service {
    private static final String TAG = "MY_SampleService";
    public static final int NOTIFICATION_ID = 1;
    public static final String ACTION_BACK = "com.example.tmd.service.ACTION_BACK";
    public static final String ACTION_PLAY_PAUSE = "com.example.tmd.service.ACTION_PLAY_PAUSE";
    public static final String ACTION_NEXT = "com.example.tmd.service.ACTION_NEXT";
    public static final String ACTION_SELECT_SONG = "com.example.tmd.service.ACTION_SELECT_SONG";
    public static final String ACTION_SEEK = "com.example.tmd.service.ACTION_SEEK";
    public static final String ACTION_GET_SONG_STATUS =
            "com.example.tmd.service.ACTION_GET_SONG_STATUS";
    public static final String ACTION_OPEN_PLAY_SONG_ACTIVITY = "com.example.tmd.service.ACTION_OPEN_PLAY_SONG_ACTIVITY";
    public static final String EXTRA_SONG_ID = "com.example.tmd.service.EXTRA_SONG_ID";
    public static final String EXTRA_RETURN_TRACK = "com.example.tmd.service.EXTRA_RETURN_TRACK";
    public static final String EXTRA_SEEK = "com.example.tmd.service.EXTRA_SEEK";
    public static final String EXTRA_TRACK = "com.example.tmd.service.EXTRA_TRACK";
    public static final String EXTRA_SONG_STATUS = "com.example.tmd.service.EXTRA_SONG_STATUS";
    public static final String EXTRA_CURRENT_PROGRESS = "com.example.tmd.service.EXTRA_CURRENT_PROGRESS";
    private Handler mHandler;
    private MediaPlayer mMediaPlayer;
    private List<TrackModel> mListTracks;
    private int mPositionTrackPlaying;
    private TrackModel mTrackModel;
    private Intent mIntent;
    private int mTimeDelay;
    private boolean mIsUpdatingProgressBar;
    private NotificationSong mNotificationSong;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationSong = new NotificationSong(this);
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
                Bundle bundle = intent.getBundleExtra(BUNDLE_LIST_TRACKS);
                mListTracks = (List<TrackModel>) bundle.getSerializable(LIST_TRACKS);
                mPositionTrackPlaying = bundle.getInt(SELECTED_TRACK, 0);
                playTrack();
                break;
            case ACTION_BACK:
                if (mPositionTrackPlaying > 0) {
                    mPositionTrackPlaying--;
                    sendProgressBroadcast(0);
                    playTrack();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Đây là track đầu tiên trong playlist",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case ACTION_PLAY_PAUSE:
                if (mMediaPlayer != null) {
                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.pause();
                        sendTrackBroadcast(ACTION_PAUSE);
                    } else {
                        mMediaPlayer.start();
                        if (!mIsUpdatingProgressBar) {
                            mIsUpdatingProgressBar = true;
                            updateProgressBar();
                        }
                        sendTrackBroadcast(ACTION_PLAY);
                    }
                }
                break;
            case ACTION_NEXT:
                if (mPositionTrackPlaying < mListTracks.size() - 1) {
                    mPositionTrackPlaying++;
                    sendProgressBroadcast(0);
                    playTrack();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Đã phát hết playlist",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case ACTION_SEEK:
                int seekPercent = intent.getIntExtra(EXTRA_SEEK, 0);
                int seekMili = convertPercentToMili(seekPercent);
                mMediaPlayer.seekTo(seekMili);
                mMediaPlayer.start();
                sendTrackBroadcast(ACTION_PLAY);
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

    private void playTrack() {
        mTrackModel = mListTracks.get(mPositionTrackPlaying);
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        streamTrack();
        mIntent = new Intent();
        mIntent.setAction(ACTION_RETURN_TRACK);
        mIntent.putExtra(EXTRA_RETURN_TRACK, mTrackModel);
        sendBroadcast(mIntent);
        mNotificationSong.updatePlayingTrack(mTrackModel);
    }

    private void streamTrack() {
        if (mTrackModel.isStreamable()) {
            new AsyncTask<Service, Void, Void>() {
                private PlayBackGroundService mService;

                @Override
                protected Void doInBackground(Service... params) {
                    mService = (PlayBackGroundService) params[0];
                    mMediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(mTrackModel
                            .getStreamUrl() + "?client_id=" + API_KEY));
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    calculateDelayTime();
                    mMediaPlayer.start();
                    mService.sendTrackBroadcast(ACTION_PLAY);
                    if (!mIsUpdatingProgressBar) {
                        mIsUpdatingProgressBar = true;
                        updateProgressBar();
                    }
                }
            }.execute(this);
        }
    }

    private void sendProgressBroadcast(int progress) {
        mIntent = new Intent();
        mIntent.setAction(ACTION_UPDATE_PROGRESSBAR);
        mIntent.putExtra(EXTRA_CURRENT_PROGRESS, progress);
        sendBroadcast(mIntent);
    }

    private void sendTrackBroadcast(String action) {
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
                    if (mMediaPlayer.isPlaying()) {
                        sendProgressBroadcast(convertMiliToPercent(mMediaPlayer.getCurrentPosition()));
                        updateProgressBar();
                    } else {
                        mIntent = new Intent();
                        mIsUpdatingProgressBar = false;
                        mIntent.setAction(ACTION_PAUSE);
                        sendBroadcast(mIntent);
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
