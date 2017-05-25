package com.tuquyet.soundcloud.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static com.tuquyet.soundcloud.service.PlayBackGroundService.ACTION_SEEK;

/**
 * Created by tmd on 23/05/2017.
 */
public class TrackReceiver extends BroadcastReceiver {
    private static final String TAG = "MY_TrackReceiver";
    public static final String ACTION_PLAY = "com.example.tmd.service.ACTION_PLAY";
    public static final String ACTION_PAUSE = "com.example.tmd.service.ACTION_PAUSE";
    public static final String ACTION_UPDATE_PROGRESSBAR = "com.example.tmd.service.ACTION_UPDATE_PROGRESSBAR";
    public static final String ACTION_RETURN_TRACK = "com.example.tmd.service.ACTION_RETURN_TRACK";
    public static final String ACTION_RETURN_SONG_STATUS = "com.example.tmd.service.ACTION_RETURN_SONG_STATUS";
    private OnReceiverListener mListener;

    public TrackReceiver(OnReceiverListener listener) {
        mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case ACTION_PLAY:
                mListener.onPause(intent);
                break;
            case ACTION_PAUSE:
                mListener.onPlay(intent);
                break;
            case ACTION_UPDATE_PROGRESSBAR:
                mListener.onUpdateProgressBar(intent);
                break;
            case ACTION_RETURN_TRACK:
                mListener.onReturnTrackFromService(intent);
                break;
            case ACTION_RETURN_SONG_STATUS:
                mListener.onReturnSongStatus(intent);
                break;
            default:
                break;
        }
    }

    public interface OnReceiverListener {
        void onPause(Intent intent);

        void onPlay(Intent intent);

        void onUpdateProgressBar(Intent intent);

        void onReturnTrackFromService(Intent intent);

        void onReturnSongStatus(Intent intent);
    }
}
