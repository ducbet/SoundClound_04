package com.tuquyet.soundcloud.service;

import com.tuquyet.soundcloud.data.model.CommentModel;
import com.tuquyet.soundcloud.data.model.PlaylistModel;
import com.tuquyet.soundcloud.data.model.TrackModel;
import com.tuquyet.soundcloud.data.model.UserModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by tmd on 13/05/2017.
 */

public interface SoundCloundService {

    @GET("playlists/{id}")
    Call<PlaylistModel> getPlaylist(@Path("id") int id, @Query("client_id") String API_KEY);

    @GET("tracks/{id}")
    Call<TrackModel> getTrack(@Path("id") int id, @Query("client_id") String API_KEY);

    @GET("comments/{id}")
    Call<CommentModel> getComment(@Path("id") int id, @Query("client_id") String API_KEY);

    @GET("users/{id}")
    Call<UserModel> getUser(@Path("id") int id, @Query("client_id") String API_KEY);

    @GET("/tracks/{id}/comments")
    Call<ArrayList<CommentModel>> getCommentOfTrack(@Path("id") int id, @Query("client_id") String API_KEY);

}
