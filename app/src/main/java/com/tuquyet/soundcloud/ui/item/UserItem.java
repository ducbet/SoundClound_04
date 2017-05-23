package com.tuquyet.soundcloud.ui.item;

/**
 * Created by tuquyet on 23/05/2017.
 */
public class UserItem {
    private int mUserAvatar;
    private String mUserName;
    private String mUserCity;
    private String mUserCountry;

    public UserItem(int userAvatar, String userName, String userCity, String userCountry) {
        mUserAvatar = userAvatar;
        mUserName = userName;
        mUserCity = userCity;
        mUserCountry = userCountry;
    }

    public int getUserAvatar() {
        return mUserAvatar;
    }

    public void setUserAvatar(int userAvatar) {
        mUserAvatar = userAvatar;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getUserCity() {
        return mUserCity;
    }

    public void setUserCity(String userCity) {
        mUserCity = userCity;
    }

    public String getUserCountry() {
        return mUserCountry;
    }

    public void setUserCountry(String userCountry) {
        mUserCountry = userCountry;
    }
}
