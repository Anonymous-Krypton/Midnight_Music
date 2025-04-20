package com.example.midnightmusic.data.network;

import com.example.midnightmusic.data.model.Song;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import java.util.List;

public interface JioSaavnService {
    String BASE_URL = "https://saavnapi-nine.vercel.app";

    @GET("result/")
    Call<List<Song>> searchSongs(
        @Query("query") String query,
        @Query("lyrics") boolean lyrics
    );

    @GET("song/")
    Call<Song> getSongDetails(
        @Query("query") String songLink,
        @Query("lyrics") boolean lyrics
    );

    @GET("playlist/")
    Call<List<Song>> getPlaylist(
        @Query("query") String playlistLink,
        @Query("lyrics") boolean lyrics
    );

    @GET("album/")
    Call<List<Song>> getAlbum(
        @Query("query") String albumLink,
        @Query("lyrics") boolean lyrics
    );

    @GET("lyrics/")
    Call<Song> getLyrics(
        @Query("query") String songLink
    );
} 