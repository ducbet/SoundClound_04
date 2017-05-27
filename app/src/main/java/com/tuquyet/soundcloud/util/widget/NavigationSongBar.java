package com.tuquyet.soundcloud.util.widget;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.tuquyet.soundcloud.data.model.TrackModel;
import com.tuquyet.soundcloud.service.PlayBackGroundService;
import com.tuquyet.soundcloud.service.TrackReceiver;

import static com.tuquyet.soundcloud.service.PlayBackGroundService.ACTION_BACK;
import static com.tuquyet.soundcloud.service.PlayBackGroundService.ACTION_GET_SONG_STATUS;
import static com.tuquyet.soundcloud.service.PlayBackGroundService.ACTION_NEXT;
import static com.tuquyet.soundcloud.service.PlayBackGroundService.ACTION_PLAY_PAUSE;
import static com.tuquyet.soundcloud.service.PlayBackGroundService.ACTION_SEEK;
import static com.tuquyet.soundcloud.service.PlayBackGroundService.EXTRA_CURRENT_PROGRESS;
import static com.tuquyet.soundcloud.service.PlayBackGroundService.EXTRA_RETURN_TRACK;
import static com.tuquyet.soundcloud.service.PlayBackGroundService.EXTRA_SEEK;
import static com.tuquyet.soundcloud.service.PlayBackGroundService.EXTRA_SONG_STATUS;
import static com.tuquyet.soundcloud.service.TrackReceiver.ACTION_PAUSE;
import static com.tuquyet.soundcloud.service.TrackReceiver.ACTION_PLAY;
import static com.tuquyet.soundcloud.service.TrackReceiver.ACTION_RETURN_SONG_STATUS;
import static com.tuquyet.soundcloud.service.TrackReceiver.ACTION_RETURN_TRACK;
import static com.tuquyet.soundcloud.service.TrackReceiver.ACTION_UPDATE_PROGRESSBAR;

/**
 * Created by tmd on 16/05/2017.
 */
public class NavigationSongBar extends LinearLayout
        implements View.OnClickListener, TrackReceiver.OnReceiverListener {

    public static final String TAG = "MY_NavigationSongBar";
    public static final int SEEK_BAR_MAX = 1000;
    private Context mContext;
    private TrackModel mTrackModel;
    private View mRootView;
    private ImageView mImgWaveformSmall;
    private ImageView mImgPlay;
    private TextView mTxtTrackInfo;
    private SeekBar mSeekBar;
    private ProgressBar mProgressBar, mProgressBarSmall;
    private LinearLayout mRelativeLayoutWaveform;
    private boolean mIsShowingWaveform = true;
    private Animation mAnimationShowWaveform, mAnimationHideWaveform;
    private Intent mIntent;
    private TrackReceiver mTrackReceiver;

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

    private void initViews() {
        mRootView = inflate(mContext, R.layout.layout_navigation_song_bar, this);
        mRelativeLayoutWaveform = (LinearLayout) findViewById(R.id.linear_layout_waveform);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar_waveform);
        mProgressBar.setMax(SEEK_BAR_MAX);
        mProgressBarSmall = (ProgressBar) findViewById(R.id.progress_bar_small);
        mProgressBarSmall.setMax(SEEK_BAR_MAX);
        mImgWaveformSmall = ((ImageView) mRootView.findViewById(R.id.image_view_waveform_small));
        mImgPlay = (ImageView) findViewById(R.id.image_view_play);

        findViewById(R.id.relative_layout_small_waveform).setOnClickListener(this);
        findViewById(R.id.image_view_previous).setOnClickListener(this);
        findViewById(R.id.image_view_play).setOnClickListener(this);
        findViewById(R.id.image_view_next).setOnClickListener(this);

        createBroadcast();
        createSeekBar();
        createShowWaveformAnim();
        createHideWaveformAnim();
        autoHideWaveform();
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
        mContext.registerReceiver(mTrackReceiver, intentFilter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_view_previous:
                mIntent = new Intent(mContext, PlayBackGroundService.class);
                mIntent.setAction(ACTION_BACK);
                mContext.startService(mIntent);
                break;
            case R.id.image_view_play:
                mIntent = new Intent(mContext, PlayBackGroundService.class);
                mIntent.setAction(ACTION_PLAY_PAUSE);
                mContext.startService(mIntent);
                break;
            case R.id.image_view_next:
                mIntent = new Intent(mContext, PlayBackGroundService.class);
                mIntent.setAction(ACTION_NEXT);
                mContext.startService(mIntent);
                break;
            case R.id.relative_layout_small_waveform:
                if (mIsShowingWaveform) hideWaveform();
                else showWaveform();
                break;
            default:
                break;
        }
    }

    private void createSeekBar() {
        mSeekBar = (SeekBar) findViewById(R.id.seek_bar_waveform);
        mSeekBar.setMax(SEEK_BAR_MAX);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mProgressBar.setProgress(progress);
                mProgressBarSmall.setProgress(progress);
                mIntent = new Intent(mContext, PlayBackGroundService.class);
                mIntent.setAction(ACTION_SEEK);
                mIntent.putExtra(EXTRA_SEEK, seekBar.getProgress());
                mContext.startService(mIntent);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void loadSongStatus() {
        mIntent = new Intent(mContext, PlayBackGroundService.class);
        mIntent.setAction(ACTION_GET_SONG_STATUS);
        mContext.startService(mIntent);
    }

    private void loadTrackInfo() {
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
                            mImgWaveformSmall.setBackground(resource);
                            invalidate();
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

    private void hideWaveform() {
        if (mIsShowingWaveform) {
            mRelativeLayoutWaveform.startAnimation(mAnimationHideWaveform);
            mIsShowingWaveform = false;
        }
    }

    private void showWaveform() {
        if (!mIsShowingWaveform) {
            mRelativeLayoutWaveform.startAnimation(mAnimationShowWaveform);
            mIsShowingWaveform = true;
        }
    }

    @Override
    public void onPause(Intent intent) {
        mImgPlay.setImageResource(R.drawable.ic_pause_white_24px);
    }

    @Override
    public void onPlay(Intent intent) {
        mImgPlay.setImageResource(R.drawable.ic_play_arrow_white_24px);
    }

    @Override
    public void onUpdateProgressBar(Intent intent) {
        mProgressBar.setProgress(intent.getIntExtra(EXTRA_CURRENT_PROGRESS, 0));
        mProgressBarSmall.setProgress(intent.getIntExtra(EXTRA_CURRENT_PROGRESS, 0));
    }

    @Override
    public void onReturnTrackFromService(Intent intent) {
        this.mTrackModel = (TrackModel) intent.getSerializableExtra(EXTRA_RETURN_TRACK);
        loadWaveform();
        loadTrackInfo();
        loadSongStatus();
    }

    @Override
    public void onReturnSongStatus(Intent intent) {
        if (intent.getBooleanExtra(EXTRA_SONG_STATUS, true)) {
            mImgPlay.setImageResource(R.drawable.ic_pause_white_24px);
        } else {
            mImgPlay.setImageResource(R.drawable.ic_play_arrow_white_24px);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mContext.unregisterReceiver(mTrackReceiver);
    }
}
