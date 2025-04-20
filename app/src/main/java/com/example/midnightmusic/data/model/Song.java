package com.example.midnightmusic.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "songs")
public class Song {
    @PrimaryKey
    @NonNull
    @SerializedName("songid")
    private String id;

    @SerializedName("song")
    private String song;

    @SerializedName("singers")
    private String singers;

    @SerializedName("album")
    private String album;

    @SerializedName("album_url")
    private String albumUrl;

    @SerializedName("duration")
    private String duration;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("language")
    private String language;

    @SerializedName("url")
    private String downloadUrl;

    @SerializedName("year")
    private String year;

    @SerializedName("lyrics")
    private String lyrics;

    @SerializedName("perma_url")
    private String permaUrl;

    @SerializedName("label")
    private String label;

    @SerializedName("has_lyrics")
    private boolean hasLyrics;

    @SerializedName("image")
    private String image;

    private boolean isLiked;
    private long timestamp;

    // Constructor
    public Song(@NonNull String id) {
        this.id = id;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters and Setters
    @NonNull
    public String getId() { return id; }
    public void setId(@NonNull String id) { this.id = id; }

    public String getSong() { return song; }
    public void setSong(String song) { this.song = song; }

    public String getSingers() { return singers; }
    public void setSingers(String singers) { this.singers = singers; }

    public String getAlbum() { return album; }
    public void setAlbum(String album) { this.album = album; }

    public String getAlbumUrl() { return albumUrl; }
    public void setAlbumUrl(String albumUrl) { this.albumUrl = albumUrl; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getImageUrl() { 
        if (imageUrl != null) return imageUrl;
        if (image != null) return image;
        return "https://wallpapercave.com/wp/wp5810499.jpg";
    }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getDownloadUrl() { return downloadUrl; }
    public void setDownloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl; }

    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }

    public String getLyrics() { return lyrics; }
    public void setLyrics(String lyrics) { this.lyrics = lyrics; }

    public String getPermaUrl() { return permaUrl; }
    public void setPermaUrl(String permaUrl) { this.permaUrl = permaUrl; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public boolean isHasLyrics() { return hasLyrics; }
    public void setHasLyrics(boolean hasLyrics) { this.hasLyrics = hasLyrics; }

    public boolean isLiked() { return isLiked; }
    public void setLiked(boolean liked) { isLiked = liked; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }
} 