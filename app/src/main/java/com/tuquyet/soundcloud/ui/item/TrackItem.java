package com.tuquyet.soundcloud.ui.item;

/**
 * Created by tuquyet on 22/05/2017.
 */
public class TrackItem {
    private int mTrackAvatar;
    private String mTrackTitle;
    private String mTrackUser;
    private String mTrackDate;
    private int mTrackWaveform;

    public TrackItem(int trackAvatar, String trackTitle, String trackUser, String trackDate,
                     int trackWaveform) {
        mTrackAvatar = trackAvatar;
        mTrackTitle = trackTitle;
        mTrackUser = trackUser;
        mTrackDate = trackDate;
        mTrackWaveform = trackWaveform;
    }

    public int getTrackAvatar() {
        return mTrackAvatar;
    }

    public void setTrackAvatar(int trackAvatar) {
        mTrackAvatar = trackAvatar;
    }

    public String getTrackTitle() {
        return mTrackTitle;
    }

    public void setTrackTitle(String trackTitle) {
        mTrackTitle = trackTitle;
    }

    public String getTrackUser() {
        return mTrackUser;
    }

    public void setTrackUser(String trackUser) {
        mTrackUser = trackUser;
    }

    public String getTrackDate() {
        return mTrackDate;
    }

    public void setTrackDate(String trackDate) {
        mTrackDate = trackDate;
    }

    public int getTrackWaveform() {
        return mTrackWaveform;
    }

    public void setTrackWaveform(int trackWaveform) {
        mTrackWaveform = trackWaveform;
    }
}
