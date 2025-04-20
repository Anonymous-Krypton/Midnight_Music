package com.example.midnightmusic.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.midnightmusic.data.db.Converters;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "playlists")
public class Playlist {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;
    private String description;
    private long createdAt;
    private long updatedAt;

    @TypeConverters(Converters.class)
    private List<String> songIds = new ArrayList<>();

    // Constructor
    public Playlist(String name) {
        this.name = name;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
    }

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }

    public List<String> getSongIds() { return songIds; }
    public void setSongIds(List<String> songIds) { this.songIds = songIds; }

    // Helper methods
    public void addSong(String songId) {
        if (!songIds.contains(songId)) {
            songIds.add(songId);
            updatedAt = System.currentTimeMillis();
        }
    }

    public void removeSong(String songId) {
        if (songIds.remove(songId)) {
            updatedAt = System.currentTimeMillis();
        }
    }

    public boolean containsSong(String songId) {
        return songIds.contains(songId);
    }

    public int getSongCount() {
        return songIds.size();
    }
} 