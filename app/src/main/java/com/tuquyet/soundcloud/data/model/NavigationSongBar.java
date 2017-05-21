package com.tuquyet.soundcloud.data.model;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tuquyet.soundcloud.R;

/**
 * Created by tmd on 16/05/2017.
 */
public class NavigationSongBar extends LinearLayout implements View.OnClickListener {
    public static final String TAG = "NavigationSongBar";
    private Context mContext;
    private TrackModel mTrackModel;
    private View mRootView;
    private ImageView mImgWaveformSmall;
    private TextView mTxtTrackInfo;
    private SeekBar mSeekBar;
    private ProgressBar mProgressBar, mProgressBarSmall;
    private LinearLayout mRelativeLayoutWaveform;
    private boolean isShowingWaveform = true;
    private Animation mAnimationShowWaveform, mAnimationHideWaveform;

    public NavigationSongBar(Context context) {
        super(context);
        mContext = context;
    }

    public NavigationSongBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initViews();
    }

    public NavigationSongBar(Context context, @Nullable AttributeSet attrs, TrackModel trackModel) {
        super(context, attrs);
        mContext = context;
        mTrackModel = trackModel;
        initViews();
    }

    public NavigationSongBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public TrackModel getTrackModel() {
        return mTrackModel;
    }

    public void setTrackModel(TrackModel trackModel) {
        this.mTrackModel = trackModel;
    }

    private void initViews() {
        mRootView = inflate(mContext, R.layout.layout_navigation_song_bar, this);
        mRelativeLayoutWaveform = (LinearLayout) findViewById(R.id.linear_layout_waveform);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgressBarSmall = (ProgressBar) findViewById(R.id.progress_bar_small);
        mImgWaveformSmall = ((ImageView) mRootView.findViewById(R.id.image_view_waveform_small));
        findViewById(R.id.relative_layout_small_waveform).setOnClickListener(this);
        findViewById(R.id.image_view_previous).setOnClickListener(this);
        findViewById(R.id.image_view_play).setOnClickListener(this);
        findViewById(R.id.image_view_next).setOnClickListener(this);

        loadSeekBar();
        createShowWaveformAnim();
        createHideWaveformAnim();
//        if (mContext instanceof PlaySongActivity) {
//           mRelativeLayoutWaveform.setVisibility(GONE);
//        }
        autoHideWaveform();
        loadWaveform();
        loadTrackInfo();
    }

    private void loadSeekBar() {
        mSeekBar = (SeekBar) findViewById(R.id.seek_bar_waveform);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mProgressBar.setProgress(progress);
                mProgressBarSmall.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void loadTrackInfo() {
        if (mTrackModel == null) return;
        mTxtTrackInfo = (TextView) findViewById(R.id.text_view_track_info);
        String trackInfo = mTrackModel.getTitle() + "\n" +
            mTrackModel.getDescription();
        mTxtTrackInfo.setText(trackInfo);
    }

    private void autoHideWaveform() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideWaveform();
            }
        }, 2000);
    }

    private void loadWaveform() {
        if (mTrackModel == null) return;
        String url = mTrackModel.getWaveformUrl();
        if (url.isEmpty()) return;
        Glide.with(mContext)
            .load(mTrackModel.getWaveformUrl())
            .centerCrop()
            .error(R.mipmap.ic_launcher)
            .placeholder(R.mipmap.ic_launcher_round)
            .into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable resource,
                                            GlideAnimation<? super GlideDrawable> glideAnimation) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        mSeekBar.setBackground(resource);
                        // sao khong thay doi duoc src?
                        mImgWaveformSmall.setImageDrawable(resource);
                    }
                }
            });
    }

    private void createHideWaveformAnim() {
        mAnimationHideWaveform = AnimationUtils.loadAnimation(mContext, R.anim.anim_hide_waveform);
        mAnimationHideWaveform.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mRelativeLayoutWaveform.setVisibility(GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private void createShowWaveformAnim() {
        mAnimationShowWaveform = AnimationUtils.loadAnimation(mContext, R.anim.anim_show_waveform);
        mAnimationShowWaveform.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mRelativeLayoutWaveform.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_view_previous:
                previousSong();
                break;
            case R.id.image_view_play:
                playSong();
                break;
            case R.id.image_view_next:
                nextSong();
                break;
            case R.id.relative_layout_small_waveform:
                if (isShowingWaveform) hideWaveform();
                else showWaveform();
                break;
            default:
                break;
        }
    }

    private void hideWaveform() {
        if (isShowingWaveform) {
            mRelativeLayoutWaveform.startAnimation(mAnimationHideWaveform);
            isShowingWaveform = false;
        }
    }

    private void showWaveform() {
        if (!isShowingWaveform) {
            mRelativeLayoutWaveform.startAnimation(mAnimationShowWaveform);
            isShowingWaveform = true;
        }
    }

    private void previousSong() {
    }

    private void playSong() {
    }

    private void nextSong() {
    }
}
