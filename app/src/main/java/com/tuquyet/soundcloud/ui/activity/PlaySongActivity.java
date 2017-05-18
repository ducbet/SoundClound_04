package com.tuquyet.soundcloud.ui.activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tuquyet.soundcloud.R;
import com.tuquyet.soundcloud.data.model.CommentModel;
import com.tuquyet.soundcloud.data.model.TrackModel;
import com.tuquyet.soundcloud.service.ServiceGenerator;
import com.tuquyet.soundcloud.service.SoundCloundService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tuquyet.soundcloud.ui.activity.MainActivity.API_KEY;

public class PlaySongActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "PlaySongActivity";
    private SoundCloundService mService;
    private TrackModel mTrackModel;
    private int mTrackId = 13158665;
    private List<CommentModel> mListComments;
    private RecyclerView mRecComments;
    private ImageView mImgArtwork;
    private TextView mTxtTrackInfo;
    private CoordinatorLayout mCoordinatorLayout;
    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        removeTittleBar();
        setContentView(R.layout.activity_play_song);
        initViews();
        getTrack();
    }

    private void initViews() {
        mService = ServiceGenerator.createService(SoundCloundService.class);
        mImgArtwork = (ImageView) findViewById(R.id.image_view_artwork);
        mTxtTrackInfo = (TextView) findViewById(R.id.text_view_track_info);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        mRecComments = (RecyclerView) findViewById(R.id.recycler_view_comment);
        findViewById(R.id.fab_home).setOnClickListener(this);
        findViewById(R.id.fab_download).setOnClickListener(this);
        findViewById(R.id.fab_watch_later).setOnClickListener(this);
        findViewById(R.id.fab_more).setOnClickListener(this);
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

    }

    private void loadImage(String url, ImageView imgTarget) {
        Glide.with(this)
            .load(url)
            .centerCrop()
            .error(R.mipmap.ic_launcher)
            .placeholder(R.mipmap.ic_launcher_round)
            .into(imgTarget);
    }

    private void getTrack() {
        mService.getTrack(mTrackId, API_KEY).enqueue(new Callback<TrackModel>() {
            @Override
            public void onResponse(Call<TrackModel> call, Response<TrackModel> response) {
                if (response != null) {
                    mTrackModel = response.body();
                    String url;
                    // load comment from internet
                    pullComment(mTrackModel);
                    // load track info
                    String trackInfo = mTrackModel.getTitle() + "\n" +
                        mTrackModel.getDescription();
                    mTxtTrackInfo.setText(trackInfo);
                    // load image artwork
                    url = mTrackModel.getArtworkUrl();
                    if (!url.isEmpty()) loadImage(url, mImgArtwork);
                }
            }

            @Override
            public void onFailure(Call<TrackModel> call, Throwable t) {
                Toast.makeText(PlaySongActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_more:
                if (mCoordinatorLayout.getVisibility() == View.VISIBLE) {
                    hideFab();
                } else {
                    showMoreFab();
                }
                break;
            case R.id.fab_home:
                break;
            case R.id.fab_download:
                break;
            case R.id.fab_watch_later:
                break;
            default:
                break;
        }
    }

    private void hideFab() {
        mCoordinatorLayout.setVisibility(View.INVISIBLE);
    }

    private void showMoreFab() {
        mCoordinatorLayout.setVisibility(View.VISIBLE);
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
}
