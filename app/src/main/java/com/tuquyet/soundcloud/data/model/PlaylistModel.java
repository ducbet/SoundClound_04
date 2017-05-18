package com.tuquyet.soundcloud.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tmd on 13/05/2017.
 */

public class PlaylistModel {

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


    public String getTitle() {
        if (mTitle == null) return "";
        return mTitle;
    }

    public String getDescription() {
        if (mDescription == null) return "";
        return mDescription;
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

    public String getArtworkUrl() {
        if (mArtworkUrl == null) return "";
        return mArtworkUrl;
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
}
