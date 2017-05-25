package com.tuquyet.soundcloud.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tmd on 13/05/2017.
 */

public class TrackModel implements Serializable {

    @SerializedName("id")
    private int mID;

    @SerializedName("created_at")
    private String mCreatedAt;

    @SerializedName("user_id")
    private int mUserId;

    @SerializedName("title")
    private String mTitle;

    @SerializedName("artwork_url")
    private String mArtworkUrl;

    @SerializedName("description")
    private String mDescription;

    @SerializedName("duration")
    private int mDuration;

    @SerializedName("genre")
    private String mGenre;

    @SerializedName("release_day")
    private int mReleaseDay;

    @SerializedName("release_month")
    private int mReleaseMonth;

    @SerializedName("release_year")
    private int mReleaseYear;

    @SerializedName("streamable")
    private boolean mStreamable;

    @SerializedName("downloadable")
    private boolean mDownloadable;

    @SerializedName("waveform_url")
    private String mWaveformUrl;

    @SerializedName("download_url")
    private String mDownloadUrl;

    @SerializedName("stream_url")
    private String mStreamUrl;

    @SerializedName("video_url")
    private String mVideoUrl;

    @SerializedName("bpm")
    private float mBpm;

    @SerializedName("commentable")
    private boolean mCommentable;

    @SerializedName("comment_count")
    private int mCommentCount;

    @SerializedName("download_count")
    private int mDownloadCount;

    @SerializedName("playback_count")
    private int mPlaybackCount;

    @SerializedName("favoritings_count")
    private int mFavoritingsCount;

    private List<CommentModel> mListComments;

    public void setListComments(List<CommentModel> listComments) {
        mListComments = listComments;
    }

    public List<CommentModel> getListComments() {
        return mListComments;
    }


    public int getID() {
        return mID;
    }

    public String getCreatedAt() {
        if (mCreatedAt == null) return "";
        return mCreatedAt;
    }

    public int getUserId() {
        return mUserId;
    }

    public String getTitle() {
        if (mTitle == null) return "";
        return mTitle;
    }

    public String getArtworkUrl() {
        if (mArtworkUrl == null) return "";
        return mArtworkUrl;
    }

    public String getDescription() {
        if (mDescription == null) return "";
        return mDescription;
    }

    public int getDuration() {
        return mDuration;
    }

    public String getGenre() {
        if (mGenre == null) return "";
        return mGenre;
    }

    public int getReleaseDay() {
        return mReleaseDay;
    }

    public int getReleaseMonth() {
        return mReleaseMonth;
    }

    public int getReleaseYear() {
        return mReleaseYear;
    }

    public boolean isStreamable() {
        return mStreamable;
    }

    public boolean isDownloadable() {
        return mDownloadable;
    }

    public String getWaveformUrl() {
        if (mWaveformUrl == null) return "";
        return mWaveformUrl;
    }

    public String getDownloadUrl() {
        if (mDownloadUrl == null) return "";
        return mDownloadUrl;
    }

    public String getStreamUrl() {
        if (mStreamUrl == null) return "";
        return mStreamUrl;
    }

    public String getVideoUrl() {
        if (mVideoUrl == null) return "";
        return mVideoUrl;
    }

    public float getBpm() {
        return mBpm;
    }

    public boolean isCommentable() {
        return mCommentable;
    }

    public int getCommentCount() {
        return mCommentCount;
    }

    public int getDownloadCount() {
        return mDownloadCount;
    }

    public int getPlaybackCount() {
        return mPlaybackCount;
    }

    public int getFavoritingsCount() {
        return mFavoritingsCount;
    }

}
