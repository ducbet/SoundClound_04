package com.tuquyet.soundcloud.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by tmd on 24/05/2017.
 */
public class ObjectListUser {
    @SerializedName("collection")
    private ArrayList<UserModel> mListUsers;

    @SerializedName("next_href")
    private String mNextPage = "";

    public ArrayList<UserModel> getListUsers() {
        return mListUsers;
    }

    public String getNextPage() {
        if (mNextPage == null) return "";
        return mNextPage;
    }
}
