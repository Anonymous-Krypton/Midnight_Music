package com.example.midnightmusic.player;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.midnightmusic.data.model.Song;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MusicPlayerManager {
    private static volatile MusicPlayerManager instance;
    private final ExoPlayer player;
    private final List<Song> queue;
    private int currentIndex;
    private PlayerCallback callback;
    private Context context;
    private boolean isShuffleOn = false;
    private int repeatMode = Player.REPEAT_MODE_OFF;
    
    private final MutableLiveData<Boolean> isPlayingLiveData = new MutableLiveData<>(false);
    private final MutableLiveData<Song> currentSongLiveData = new MutableLiveData<>();
    private final MutableLiveData<Long> currentPosition = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isShuffleEnabled = new MutableLiveData<>(false);
    private final MutableLiveData<Integer> repeatModeState = new MutableLiveData<>(Player.REPEAT_MODE_OFF);

    public interface PlayerCallback {
        void onPlaybackStateChanged(boolean isPlaying);
        void onSongChanged(Song song);
    }

    private MusicPlayerManager(Context context) {
        this.context = context.getApplicationContext();
        player = new ExoPlayer.Builder(context).build();
        queue = new ArrayList<>();
        currentIndex = -1;

        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                if (callback != null) {
                    callback.onPlaybackStateChanged(player.isPlaying());
                }
                isPlayingLiveData.postValue(player.isPlaying());
            }

            @Override
            public void onMediaItemTransition(MediaItem mediaItem, int reason) {
                if (currentIndex >= 0 && currentIndex < queue.size()) {
                    Song song = queue.get(currentIndex);
                    if (callback != null) {
                        callback.onSongChanged(song);
                    }
                    currentSongLiveData.postValue(song);
                }
            }
        });
    }

    public static MusicPlayerManager getInstance(Context context) {
        if (instance == null) {
            synchronized (MusicPlayerManager.class) {
                if (instance == null) {
                    instance = new MusicPlayerManager(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    public void setCallback(PlayerCallback callback) {
        this.callback = callback;
    }

    public ExoPlayer getPlayer() {
        return player;
    }

    public void playSong(Song song) {
        queue.clear();
        queue.add(song);
        currentIndex = 0;
        playCurrentSong();
    }

    public void playQueue(List<Song> songs, int startIndex) {
        queue.clear();
        queue.addAll(songs);
        currentIndex = startIndex;
        playCurrentSong();
    }

    public void addToQueue(Song song) {
        queue.add(song);
        if (queue.size() == 1) {
            currentIndex = 0;
            playCurrentSong();
        }
    }

    public void queueNext(Song song) {
        if (currentIndex >= 0) {
            queue.add(currentIndex + 1, song);
        } else {
            queue.add(song);
            currentIndex = 0;
            playCurrentSong();
        }
    }

    public void skipToNext() {
        if (currentIndex < queue.size() - 1) {
            currentIndex++;
            playCurrentSong();
        }
    }

    public void skipToPrevious() {
        if (currentIndex > 0) {
            currentIndex--;
            playCurrentSong();
        }
    }

    private void playCurrentSong() {
        if (currentIndex >= 0 && currentIndex < queue.size()) {
            Song song = queue.get(currentIndex);
            MediaItem mediaItem = MediaItem.fromUri(Uri.parse(song.getMediaUrl()));
            player.setMediaItem(mediaItem);
            player.prepare();
            player.play();
        }
    }

    public void togglePlayPause() {
        if (player.isPlaying()) {
            player.pause();
        } else {
            player.play();
        }
    }

    public boolean isPlaying() {
        return player.isPlaying();
    }

    public Song getCurrentSong() {
        return currentIndex >= 0 && currentIndex < queue.size() ? queue.get(currentIndex) : null;
    }

    public List<Song> getQueue() {
        return new ArrayList<>(queue);
    }

    public void clearQueue() {
        queue.clear();
        currentIndex = -1;
        player.stop();
        player.clearMediaItems();
    }

    public void release() {
        player.release();
    }

    public void toggleShuffle() {
        isShuffleOn = !isShuffleOn;
        isShuffleEnabled.postValue(isShuffleOn);
        if (isShuffleOn && !queue.isEmpty()) {
            shuffleQueue(currentIndex);
        }
    }

    public void toggleRepeatMode() {
        switch (repeatMode) {
            case Player.REPEAT_MODE_OFF:
                repeatMode = Player.REPEAT_MODE_ONE;
                break;
            case Player.REPEAT_MODE_ONE:
                repeatMode = Player.REPEAT_MODE_ALL;
                break;
            default:
                repeatMode = Player.REPEAT_MODE_OFF;
                break;
        }
        player.setRepeatMode(repeatMode);
        repeatModeState.postValue(repeatMode);
    }

    private void shuffleQueue(int startIndex) {
        if (startIndex >= 0 && startIndex < queue.size()) {
            Song currentSong = queue.remove(startIndex);
            Collections.shuffle(queue);
            queue.add(0, currentSong);
            currentIndex = 0;
        }
    }

    public void seekTo(long position) {
        player.seekTo(position);
    }

    // LiveData getters with renamed methods
    public LiveData<Boolean> getPlayingLiveData() { return isPlayingLiveData; }
    public LiveData<Song> getCurrentSongLiveData() { return currentSongLiveData; }
    public LiveData<Long> getCurrentPosition() { return currentPosition; }
    public LiveData<Boolean> isShuffleEnabled() { return isShuffleEnabled; }
    public LiveData<Integer> getRepeatMode() { return repeatModeState; }
    
    public long getDuration() {
        return player != null ? player.getDuration() : 0;
    }
} 