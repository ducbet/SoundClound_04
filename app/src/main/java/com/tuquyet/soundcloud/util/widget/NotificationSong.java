package com.tuquyet.soundcloud.util.widget;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Build;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tuquyet.soundcloud.R;
import com.tuquyet.soundcloud.data.model.TrackModel;
import com.tuquyet.soundcloud.service.PlayBackGroundService;
import com.tuquyet.soundcloud.service.TrackReceiver;
import com.tuquyet.soundcloud.ui.activity.PlaySongActivity;

import static com.tuquyet.soundcloud.service.PlayBackGroundService.ACTION_BACK;
import static com.tuquyet.soundcloud.service.PlayBackGroundService.ACTION_NEXT;
import static com.tuquyet.soundcloud.service.PlayBackGroundService.ACTION_OPEN_PLAY_SONG_ACTIVITY;
import static com.tuquyet.soundcloud.service.PlayBackGroundService.ACTION_PLAY_PAUSE;
import static com.tuquyet.soundcloud.service.PlayBackGroundService.ACTION_SEEK;
import static com.tuquyet.soundcloud.service.PlayBackGroundService.EXTRA_CURRENT_PROGRESS;
import static com.tuquyet.soundcloud.service.PlayBackGroundService.NOTIFICATION_ID;
import static com.tuquyet.soundcloud.service.TrackReceiver.ACTION_PAUSE;
import static com.tuquyet.soundcloud.service.TrackReceiver.ACTION_PLAY;
import static com.tuquyet.soundcloud.service.TrackReceiver.ACTION_RETURN_SONG_STATUS;
import static com.tuquyet.soundcloud.service.TrackReceiver.ACTION_RETURN_TRACK;
import static com.tuquyet.soundcloud.service.TrackReceiver.ACTION_UPDATE_PROGRESSBAR;
import static com.tuquyet.soundcloud.util.widget.NavigationSongBar.SEEK_BAR_MAX;

/**
 * Created by tmd on 26/05/2017.
 */
public class NotificationSong implements TrackReceiver.OnReceiverListener {
    public static final String TAG = "MY_NotificationSong";
    private Service mService;
    private RemoteViews mViews;
    private TrackModel mTrackModel;
    private TrackReceiver mTrackReceiver;
    private Notification.Builder mBuilder;
    private Notification mNotification;

    public NotificationSong(Service service) {
        mService = service;
        createBroadcast();
        mViews = new RemoteViews(service.getPackageName(),
                R.layout.notification_status);
        Intent previousIntent = new Intent(service, PlayBackGroundService.class);
        previousIntent.setAction(ACTION_BACK);
        PendingIntent ppreviousIntent = PendingIntent.getService(service, 0,
                previousIntent, 0);
        mViews.setOnClickPendingIntent(R.id.image_view_notifi_previous, ppreviousIntent);
        Intent playIntent = new Intent(service, PlayBackGroundService.class);
        playIntent.setAction(ACTION_PLAY_PAUSE);
        PendingIntent pplayIntent = PendingIntent.getService(service, 0,
                playIntent, 0);
        mViews.setOnClickPendingIntent(R.id.image_view_notifi_play_pause, pplayIntent);
        Intent nextIntent = new Intent(service, PlayBackGroundService.class);
        nextIntent.setAction(ACTION_NEXT);
        PendingIntent pnextIntent = PendingIntent.getService(service, 0,
                nextIntent, 0);
        mViews.setOnClickPendingIntent(R.id.image_view_notifi_next, pnextIntent);
        mBuilder = new Notification.Builder(mService);
        if (Build.VERSION.SDK_INT >= 23) {
            mBuilder.setContent(mViews)
                    .setSmallIcon(R.drawable.soundcloud);
        } else {
            mBuilder.setContent(mViews);
        }
        mNotification = mBuilder.build();
        ((NotificationManager) mService.getSystemService(Context.NOTIFICATION_SERVICE))
                .notify(NOTIFICATION_ID, mNotification);
        service.startForeground(NOTIFICATION_ID, mNotification);
    }

    public void updatePlayingTrack(TrackModel trackModel) {
        mTrackModel = trackModel;
        updateTitle();
        updateAvatar();
        ((NotificationManager) mService.getSystemService(Context.NOTIFICATION_SERVICE)).
                notify(NOTIFICATION_ID, mNotification);
    }

    private void updateTitle() {
        mViews.setTextViewText(R.id.text_view_notifi_track_title, mTrackModel.getTitle());
    }

    private void updateAvatar() {
        Glide.with(mService)
                .load(mTrackModel.getArtworkUrl())
                .asBitmap()
                .placeholder(R.drawable.soundcloud)
                .error(R.drawable.soundcloud)
                .into(new SimpleTarget<Bitmap>(100, 100) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        mViews.setImageViewBitmap(R.id.image_view_notifi_track_avatar, resource);
                    }
                });
    }

    private void createBroadcast() {
        mTrackReceiver = new TrackReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_SEEK);
        intentFilter.addAction(ACTION_PAUSE);
        intentFilter.addAction(ACTION_PLAY);
        intentFilter.addAction(ACTION_UPDATE_PROGRESSBAR);
        intentFilter.addAction(ACTION_RETURN_TRACK);
        intentFilter.addAction(ACTION_RETURN_SONG_STATUS);
        mService.registerReceiver(mTrackReceiver, intentFilter);
    }

    @Override
    public void onPause(Intent intent) {
        mViews.setImageViewResource(R.id.image_view_notifi_play_pause,
                R.drawable.ic_pause_white_24px);
        ((NotificationManager) mService.getSystemService(Context.NOTIFICATION_SERVICE))
                .notify(NOTIFICATION_ID, mNotification);
    }

    @Override
    public void onPlay(Intent intent) {
        mViews.setImageViewResource(R.id.image_view_notifi_play_pause,
                R.drawable.ic_play_arrow_white_24px);
        ((NotificationManager) mService.getSystemService(Context.NOTIFICATION_SERVICE))
                .notify(NOTIFICATION_ID, mNotification);
    }

    @Override
    public void onUpdateProgressBar(Intent intent) {
        mViews.setProgressBar(R.id.progress_bar_notifi, SEEK_BAR_MAX, intent.getIntExtra
                (EXTRA_CURRENT_PROGRESS, 0), false);
        ((NotificationManager) mService.getSystemService(Context.NOTIFICATION_SERVICE))
                .notify(NOTIFICATION_ID, mNotification);
    }

    @Override
    public void onReturnTrackFromService(Intent intent) {
    }

    @Override
    public void onReturnSongStatus(Intent intent) {
    }
}
