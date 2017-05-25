package com.tuquyet.soundcloud.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
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
import static com.tuquyet.soundcloud.service.PlayBackGroundService.EXTRA_SONG_ID;
import static com.tuquyet.soundcloud.service.PlayBackGroundService.EXTRA_TRACK;
import static com.tuquyet.soundcloud.ui.activity.MainActivity.API_KEY;

public class PlaySongActivity extends AppCompatActivity {
    public static final String TAG = "MY_PlaySongActivity";
    private SoundCloundService mService;
    private TrackModel mTrackModel;
    private int mTrackId = 13158665;
    private List<CommentModel> mListComments;
    private CommentAdapter mCommentAdapter;
    private RecyclerView mRecComments;
    private ImageView mImgArtwork;
    private Intent mIntent;

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
        mRecComments = (RecyclerView) findViewById(R.id.recycler_view_comment);
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
        mService.getTrack(mTrackId, API_KEY).enqueue(new Callback<TrackModel>() {
            @Override
            public void onResponse(Call<TrackModel> call, Response<TrackModel> response) {
                if (response != null) {
                    mTrackModel = response.body();
                    String url;
                    pullComment(mTrackModel);

                    loadImage(mTrackModel.getArtworkUrl(), mImgArtwork);

                    sendTrackToService();
                }
            }

            @Override
            public void onFailure(Call<TrackModel> call, Throwable t) {
                Toast.makeText(PlaySongActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendTrackToService() {
        mIntent = new Intent(this, PlayBackGroundService.class);
        mIntent.setAction(ACTION_SELECT_SONG);
        mIntent.putExtra(EXTRA_TRACK, mTrackModel);
        mIntent.putExtra(EXTRA_SONG_ID, R.raw.gui_anh_xa_nho_bich_phuong);
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
}
