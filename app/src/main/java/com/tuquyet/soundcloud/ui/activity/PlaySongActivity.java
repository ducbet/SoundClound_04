package com.tuquyet.soundcloud.ui.activity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import com.tuquyet.soundcloud.ui.adapter.CommentAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tuquyet.soundcloud.service.PlayBackGroundService.ACTION_SELECT_SONG;
import static com.tuquyet.soundcloud.ui.activity.MainActivity.API_KEY;
import static com.tuquyet.soundcloud.ui.adapter.TrackAdapter.BUNDLE_LIST_TRACKS;
import static com.tuquyet.soundcloud.ui.adapter.TrackAdapter.LIST_TRACKS;
import static com.tuquyet.soundcloud.ui.adapter.TrackAdapter.SELECTED_TRACK;

public class PlaySongActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "MY_PlaySongActivity";
    private SoundCloundService mService;
    private List<TrackModel> mListTracks;
    private TrackModel mTrackModel;
    private int mTrackId = 13158665;
    private List<CommentModel> mListComments;
    private CommentAdapter mCommentAdapter;
    private RecyclerView mRecComments;
    private ImageView mImgArtwork;
    private ImageView mImgDơwnload;
    private ImageView mImgFavorite;
    private TextView mTextFavoriteCount;
    private TextView mTextDownloadCount;
    private TextView mTextCommentCount;
    private Intent mIntent;
    private DownloadManager mDownloadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        removeTittleBar();
        setContentView(R.layout.activity_play_song);
        initViews();
        getTrack();
        getCount();
        mImgDơwnload.setOnClickListener(this);
        mImgFavorite.setOnClickListener(this);
    }

    private void initViews() {
        mService = ServiceGenerator.createService(SoundCloundService.class);
        mImgArtwork = (ImageView) findViewById(R.id.image_view_artwork);
        mRecComments = (RecyclerView) findViewById(R.id.recycler_view_comment);
        mImgDơwnload = (ImageView) findViewById(R.id.image_view_download);
        mImgFavorite = (ImageView) findViewById(R.id.image_view_favorite);
        mTextFavoriteCount = (TextView) findViewById(R.id.text_view_favorite_count);
        mTextDownloadCount = (TextView) findViewById(R.id.text_view_download_count);
        mTextCommentCount = (TextView) findViewById(R.id.text_view_comment_count);
    }

    private void getCount() {
        mTextFavoriteCount.setText(String.valueOf(mTrackModel.getFavoritingsCount()));
        mTextCommentCount.setText(String.valueOf(mTrackModel.getCommentCount()));
        mTextDownloadCount.setText(String.valueOf(mTrackModel.getDownloadCount()));
    }

    private void pullComment(final TrackModel trackModel) {
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
            .error(R.drawable.soundcloud)
            .placeholder(R.mipmap.ic_launcher_round)
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
            sendListTrackToService(bundle);
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
            | View.SYSTEM_UI_FLAG_FULLSCREEN //hide Status Bar (thanh trên cùng)
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY; // quay trở lại trạng thái ẩn nếu không chạm nữa
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_view_download: {
                String url = mTrackModel.getStreamUrl() + "?client_id=" + API_KEY;
                if (mTrackModel.isDownloadable()) {
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
                    int downloadCount = mTrackModel.getDownloadCount();
                    mTrackModel.setDownloadCount(downloadCount++);
                    mTextDownloadCount.setText(String.valueOf(downloadCount));
                } else {
                    Toast.makeText(this, "This track is not downloadable !", Toast.LENGTH_SHORT)
                        .show();
                }
            }
            case R.id.image_view_favorite: {
                mImgFavorite.setImageResource(R.drawable.ic_favorite_red_24px);
                int favoriteCount = mTrackModel.getFavoritingsCount();
                mTrackModel.setFavoritingsCount(favoriteCount++);
                mTextFavoriteCount.setText(String.valueOf(favoriteCount));
            }
        }
    }
}
