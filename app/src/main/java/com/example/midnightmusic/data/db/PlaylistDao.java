package com.example.midnightmusic.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.midnightmusic.data.model.Playlist;

import java.util.List;

@Dao
public interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Playlist playlist);

    @Update
    void update(Playlist playlist);

    @Delete
    void delete(Playlist playlist);

    @Query("SELECT * FROM playlists WHERE id = :playlistId")
    LiveData<Playlist> getPlaylistById(long playlistId);

    @Query("SELECT * FROM playlists ORDER BY updatedAt DESC")
    LiveData<List<Playlist>> getAllPlaylists();

    @Query("SELECT * FROM playlists WHERE name LIKE :query ORDER BY updatedAt DESC")
    LiveData<List<Playlist>> searchPlaylists(String query);

    @Query("UPDATE playlists SET name = :newName WHERE id = :playlistId")
    void updatePlaylistName(long playlistId, String newName);

    @Query("SELECT COUNT(*) FROM playlists")
    LiveData<Integer> getPlaylistCount();
} 