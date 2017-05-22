package com.tuquyet.soundcloud.ui.item;

/**
 * Created by tuquyet on 22/05/2017.
 */
public class PlaylistItem {
    private int mPlaylistAvatar;
    private String mPlaylistTitle;
    private String mPlaylistUser;
    private String mPlaylistDate;
    private String mPlaylistDescription;

    public PlaylistItem(int playlistAvatar, String playlistTitle, String playlistUser,
                        String playlistDate, String playlistDescription) {
        mPlaylistAvatar = playlistAvatar;
        mPlaylistTitle = playlistTitle;
        mPlaylistUser = playlistUser;
        mPlaylistDate = playlistDate;
        mPlaylistDescription = playlistDescription;
    }

    public int getPlaylistAvatar() {
        return mPlaylistAvatar;
    }

    public void setPlaylistAvatar(int playlistAvatar) {
        mPlaylistAvatar = playlistAvatar;
    }

    public String getPlaylistTitle() {
        return mPlaylistTitle;
    }

    public void setPlaylistTitle(String playlistTitle) {
        mPlaylistTitle = playlistTitle;
    }

    public String getPlaylistUser() {
        return mPlaylistUser;
    }

    public void setPlaylistUser(String playlistUser) {
        mPlaylistUser = playlistUser;
    }

    public String getPlaylistDate() {
        return mPlaylistDate;
    }

    public void setPlaylistDate(String playlistDate) {
        mPlaylistDate = playlistDate;
    }

    public String getPlaylistDescription() {
        return mPlaylistDescription;
    }

    public void setPlaylistDescription(String playlistDescription) {
        mPlaylistDescription = playlistDescription;
    }
}
