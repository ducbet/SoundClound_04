package com.tuquyet.soundcloud.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tmd on 13/05/2017.
 */

public class CommentModel {

    @SerializedName("id")
    private int mID;

    @SerializedName("created_at")
    private String mCreatedAt;

    @SerializedName("body")
    private String mBody;

    @SerializedName("user_id")
    private int mUserId;

    @SerializedName("track_id")
    private int mTrackId;

    public int getID() {
        return mID;
    }

    public String getCreatedAt() {
        if (mCreatedAt == null) return "";
        return mCreatedAt;
    }

    public String getBody() {
        if (mBody == null) return "";
        return mBody;
    }

    public int getUserId() {
        return mUserId;
    }

    public int getTrackId() {
        return mTrackId;
    }
}
