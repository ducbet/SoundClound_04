package com.tuquyet.soundcloud.ui.activity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tuquyet.soundcloud.R;
import com.tuquyet.soundcloud.data.model.CommentModel;
import com.tuquyet.soundcloud.data.model.TrackModel;
import com.tuquyet.soundcloud.service.PlayBackGroundService;
import com.tuquyet.soundcloud.service.ServiceGenerator;
import com.tuquyet.soundcloud.service.SoundCloundService;
import com.tuquyet.soundcloud.service.TrackReceiver;
import com.tuquyet.soundcloud.ui.adapter.CommentAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tuquyet.soundcloud.service.PlayBackGroundService.ACTION_SEEK;
import static com.tuquyet.soundcloud.service.PlayBackGroundService.ACTION_SELECT_SONG;
import static com.tuquyet.soundcloud.service.PlayBackGroundService.EXTRA_RETURN_TRACK;
import static com.tuquyet.soundcloud.service.TrackReceiver.ACTION_PAUSE;
import static com.tuquyet.soundcloud.service.TrackReceiver.ACTION_PLAY;
import static com.tuquyet.soundcloud.service.TrackReceiver.ACTION_RETURN_SONG_STATUS;
import static com.tuquyet.soundcloud.service.TrackReceiver.ACTION_RETURN_TRACK;
import static com.tuquyet.soundcloud.service.TrackReceiver.ACTION_UPDATE_PROGRESSBAR;
import static com.tuquyet.soundcloud.ui.activity.MainActivity.API_KEY;
import static com.tuquyet.soundcloud.ui.adapter.TrackAdapter.BUNDLE_LIST_TRACKS;
import static com.tuquyet.soundcloud.ui.adapter.TrackAdapter.LIST_TRACKS;
import static com.tuquyet.soundcloud.ui.adapter.TrackAdapter.SELECTED_TRACK;

public class PlaySongActivity extends AppCompatActivity
        implements View.OnClickListener, TrackReceiver.OnReceiverListener {
    public static final String TAG = "MY_PlaySongActivity";
    private static int REQUEST_WRITE_EXTERNAL_STORAGE = 123;
    private SoundCloundService mService;
    private List<TrackModel> mListTracks;
    private TrackModel mTrackModel;
    private int mTrackId = 13158665;
    private List<CommentModel> mListComments;
    private CommentAdapter mCommentAdapter;
    private RecyclerView mRecComments;
    private ImageView mImgArtwork;
    private ImageView mImgDownload;
    private TextView mTxtTrackInfo;
    private ImageView mImgFavorite;
    private Intent mIntent;
    private DownloadManager mDownloadManager;
    private TrackReceiver mTrackReceiver;
    private Animation mAnimRotateArtwork;
    private Animation mAnimRotateTurntableSeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        removeTittleBar();
        setContentView(R.layout.activity_play_song);
        initViews();
        getTrack();
        createBroadcast();
        mImgDownload.setOnClickListener(this);
        mImgFavorite.setOnClickListener(this);
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
        registerReceiver(mTrackReceiver, intentFilter);
    }

    private void initViews() {
        mService = ServiceGenerator.createService(SoundCloundService.class);
        mImgArtwork = (ImageView) findViewById(R.id.image_view_artwork);
        mRecComments = (RecyclerView) findViewById(R.id.recycler_view_comment);
        mTxtTrackInfo = (TextView) findViewById(R.id.text_view_track_info_play_act);
        mImgDownload = (ImageView) findViewById(R.id.image_view_download);
        mImgFavorite = (ImageView) findViewById(R.id.image_view_favorite);
        mAnimRotateArtwork = AnimationUtils.loadAnimation(this, R.anim.anim_rotate_artwork);
        mAnimRotateTurntableSeek =
                AnimationUtils.loadAnimation(this, R.anim.anim_rotate_turntable_seek);
    }

    private void pullComment(final TrackModel trackModel) {
        if (trackModel.getListComments() == null) {
            mService.getCommentOfTrack(trackModel.getID(), API_KEY)
                    .enqueue(new Callback<ArrayList<CommentModel>>() {
                        @Override
                        public void onResponse(Call<ArrayList<CommentModel>> call,
                                               Response<ArrayList<CommentModel>> response) {
                            if (response != null) {
                                trackModel.setListComments(response.body());
                                loadComment();
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<CommentModel>> call, Throwable t) {
                            Toast.makeText(PlaySongActivity.this, t.getMessage(), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
        } else {
            mListComments = trackModel.getListComments();
        }
    }

    private void loadTitle(TrackModel trackModel) {
        String trackInfo = mTrackModel.getTitle() + "\n" +
                mTrackModel.getDescription();
        mTxtTrackInfo.setText(trackInfo);
    }

    private void loadComment() {
        mListComments = mTrackModel.getListComments();
        mCommentAdapter = new CommentAdapter(this, mListComments);
        mRecComments
                .setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecComments.setAdapter(mCommentAdapter);
    }

    private void loadImage(String url, ImageView imgTarget) {
        Glide.with(this)
                .load(url)
                .centerCrop()
                .error(R.drawable.sound_clound_2)
                .placeholder(R.drawable.sound_clound_2)
                .into(imgTarget);
    }

    private void getTrack() {
        mIntent = getIntent();
        if (mIntent != null) {
            Bundle bundle = mIntent.getBundleExtra(BUNDLE_LIST_TRACKS);
            mListTracks = (List<TrackModel>) bundle.getSerializable(LIST_TRACKS);
            mTrackModel = mListTracks.get(bundle.getInt(SELECTED_TRACK, 0));
            pullComment(mTrackModel);
            loadImage(mTrackModel.getArtworkUrl(), mImgArtwork);
            loadTitle(mTrackModel);
            sendListTrackToService(bundle);
            mImgArtwork.startAnimation(mAnimRotateArtwork);
            findViewById(R.id.image_view_turntable_seek).setAnimation(mAnimRotateTurntableSeek);
        }
    }

    private void sendListTrackToService(Bundle bundle) {
        mIntent = new Intent(this, PlayBackGroundService.class);
        mIntent.setAction(ACTION_SELECT_SONG);
        mIntent.putExtra(BUNDLE_LIST_TRACKS, bundle);
        startService(mIntent);
    }

    public void removeTittleBar() {
        //remove title bar,..//phai de truoc setContentView(R.layout.activity_animation);
        getSupportActionBar().hide();//hide Title Bar (tên application)
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION //hide Navigation Bar (thanh dưới cùng)
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY; // quay trở lại trạng thái ẩn nếu không chạm nữa
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void downloadTrack() {
        String url = mTrackModel.getStreamUrl() + "?client_id=" + API_KEY;
        Toast.makeText(this, "Download track " + mTrackModel.getTitle() + " ...",
                Toast.LENGTH_SHORT)
                .show();
        mDownloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setDescription(getResources().getText(R.string.download_notification))
                .setTitle(mTrackModel.getTitle());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(
                    DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                mTrackModel.getTitle() + ".mp3");
        DownloadManager manager =
                (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
        mImgDownload.setImageResource(R.drawable.ic_file_download_green_24px);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadTrack();
            } else {
                Toast.makeText(this, "PERMISSION_DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void requestPermission() {
        int isGrant = ContextCompat.checkSelfPermission(PlaySongActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (isGrant == PackageManager.PERMISSION_GRANTED) {
            downloadTrack();
        } else if (isGrant == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(PlaySongActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder aBuiler = new AlertDialog.Builder(PlaySongActivity.this)
                        .setTitle("Request write external storage Permission")
                        .setMessage("We want to access to your external storage to save this track")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(
                                        PlaySongActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        REQUEST_WRITE_EXTERNAL_STORAGE);
                            }
                        })
                        .setNegativeButton("NO", null);
                aBuiler.create().show();
                int downloadCount = mTrackModel.getDownloadCount();
                mTrackModel.setDownloadCount(downloadCount++);
            } else {
                ActivityCompat.requestPermissions(
                        PlaySongActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_view_download: {
                if (mTrackModel.isDownloadable()) {
                    requestPermission();
                } else {
                    Toast.makeText(this, "This track is not downloadable !",
                            Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case R.id.image_view_favorite: {
                if (!(mTrackModel.isFavorited())) {
                    mImgFavorite.setImageResource(R.drawable.ic_favorite_red_24px);
                    int favoriteCount = mTrackModel.getFavoritingsCount();
                    mTrackModel.setFavoritingsCount(favoriteCount++);
                }
                mTrackModel.setFavorited(true);
            }
            break;

            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mTrackReceiver);
    }

    @Override
    public void onPause(Intent intent) {

    }

    @Override
    public void onPlay(Intent intent) {

    }

    @Override
    public void onUpdateProgressBar(Intent intent) {

    }

    @Override
    public void onReturnTrackFromService(Intent intent) {
        mTrackModel = (TrackModel) intent.getSerializableExtra(EXTRA_RETURN_TRACK);
        if (mTrackModel == null) return;
        pullComment(mTrackModel);
        loadImage(mTrackModel.getArtworkUrl(), mImgArtwork);
        loadTitle(mTrackModel);
    }

    @Override
    public void onReturnSongStatus(Intent intent) {

    }
}
