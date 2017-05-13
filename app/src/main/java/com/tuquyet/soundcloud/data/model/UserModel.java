package com.tuquyet.soundcloud.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tmd on 13/05/2017.
 */

public class UserModel {

    @SerializedName("id")
    private int mID;

    @SerializedName("username")
    private String mUsername;

    @SerializedName("avatar_url")
    private String mAvatarUrl;

    @SerializedName("country")
    private String mCountry;

    @SerializedName("full_name")
    private String mFullName;

    @SerializedName("city")
    private String mCity;

    @SerializedName("description")
    private String mDescription;

    @SerializedName("online")
    private boolean mOnline;

    @SerializedName("track_count")
    private int mTrackCount;

    @SerializedName("playlist_count")
    private int mPlaylistCount;

    @SerializedName("followers_count")
    private int mFollowersCount;

    @SerializedName("followings_count")
    private int mFollowingsCount;

    @SerializedName("public_favorites_count")
    private int mPublicFavoritesCount;

    public int getID() {
        return mID;
    }

    public String getUsername() {
        if (mUsername == null) return "";
        return mUsername;
    }

    public String getAvatarUrl() {
        if (mAvatarUrl == null) return "";
        return mAvatarUrl;
    }

    public String getCountry() {
        if (mCountry == null) return "";
        return mCountry;
    }

    public String getFullName() {
        if (mFullName == null) return "";
        return mFullName;
    }

    public String getCity() {
        if (mCity == null) return "";
        return mCity;
    }

    public String getDescription() {
        if (mDescription == null) return "";
        return mDescription;
    }

    public boolean isOnline() {
        return mOnline;
    }

    public int getTrackCount() {
        return mTrackCount;
    }

    public int getPlaylistCount() {
        return mPlaylistCount;
    }

    public int getFollowersCount() {
        return mFollowersCount;
    }

    public int getFollowingsCount() {
        return mFollowingsCount;
    }

    public int getPublicFavoritesCount() {
        return mPublicFavoritesCount;
    }
}
