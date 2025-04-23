package com.example.midnightmusic.ui.player;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.SeekBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.media3.common.Player;
import androidx.palette.graphics.Palette;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.midnightmusic.R;
import com.example.midnightmusic.data.model.Song;
import com.example.midnightmusic.databinding.ActivityPlayerBinding;
import com.example.midnightmusic.player.MusicPlayerManager;
import jp.wasabeef.glide.transformations.BlurTransformation;
import android.transition.Transition;
import android.transition.TransitionInflater;

public class PlayerActivity extends AppCompatActivity {
    private ActivityPlayerBinding binding;
    private MusicPlayerManager playerManager;
    private Handler handler;
    private boolean isUserSeeking = false;
    private static final int UPDATE_INTERVAL = 1000; // 1 second

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Set up transitions
        getWindow().setSharedElementEnterTransition(
            TransitionInflater.from(this).inflateTransition(android.R.transition.move)
        );
        getWindow().setSharedElementReturnTransition(
            TransitionInflater.from(this).inflateTransition(android.R.transition.move)
        );
        
        // Postpone the transition until all shared elements are ready
        postponeEnterTransition();
        
        binding = ActivityPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        playerManager = MusicPlayerManager.getInstance(this);
        handler = new Handler(Looper.getMainLooper());

        setupUI();
        observePlayerState();
        startProgressUpdate();
        
        // Start the transition once everything is loaded
        startPostponedEnterTransition();
    }

    private void setupUI() {
        binding.btnClose.setOnClickListener(v -> {
            finishAfterTransition(); // Use this instead of finish() for smooth transition
        });
        
        binding.btnPlayPause.setOnClickListener(v -> playerManager.togglePlayPause());
        binding.btnNext.setOnClickListener(v -> playerManager.skipToNext());
        binding.btnPrevious.setOnClickListener(v -> playerManager.skipToPrevious());
        
        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    binding.txtCurrentTime.setText(formatTime(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isUserSeeking = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isUserSeeking = false;
                playerManager.seekTo(seekBar.getProgress());
            }
        });

        // Set initial play/pause button state
        updatePlayPauseButton(playerManager.isPlaying());
        
        // Set transition names for shared elements
        binding.imgAlbumArt.setTransitionName("transition_album_art");
        binding.txtSongName.setTransitionName("transition_song_title");
        binding.txtArtistName.setTransitionName("transition_artist_name");
        binding.btnPlayPause.setTransitionName("transition_play_button");
    }

    private void observePlayerState() {
        playerManager.getCurrentSongLiveData().observe(this, this::updateSongUI);
        playerManager.getPlayingLiveData().observe(this, this::updatePlayPauseButton);
        
        // Observe player position updates
        playerManager.getCurrentPosition().observe(this, position -> {
            if (!isUserSeeking && position != null) {
                binding.seekBar.setProgress(position.intValue());
                binding.txtCurrentTime.setText(formatTime(position));
            }
        });
    }

    private void updatePlayPauseButton(boolean isPlaying) {
        binding.btnPlayPause.setImageResource(
            isPlaying ? R.drawable.ic_pause : R.drawable.ic_play
        );
    }

    private void updateSongUI(Song song) {
        if (song == null) return;
        
        binding.txtSongName.setText(song.getSong());
        binding.txtArtistName.setText(song.getSingers());
        
        // Load album art
        Glide.with(this)
            .asBitmap()
            .load(song.getImageUrl())
            .apply(RequestOptions.bitmapTransform(new RoundedCorners(16)))
            .into(binding.imgAlbumArt);

        // Load blurred background
        Glide.with(this)
            .load(song.getImageUrl())
            .transform(new BlurTransformation(25, 3))
            .into(binding.backgroundImage);

        // Extract dominant color for UI accents
        Glide.with(this)
            .asBitmap()
            .load(song.getImageUrl())
            .into(new com.bumptech.glide.request.target.SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap bitmap, com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                    Palette.from(bitmap).generate(palette -> {
                        if (palette != null) {
                            int dominantColor = palette.getDominantColor(
                                ContextCompat.getColor(PlayerActivity.this, R.color.accent)
                            );
                            binding.seekBar.setProgressTintList(android.content.res.ColorStateList.valueOf(dominantColor));
                            binding.seekBar.setThumbTintList(android.content.res.ColorStateList.valueOf(dominantColor));
                        }
                    });
                }
            });

        // Update duration immediately if available
        long duration = playerManager.getDuration();
        if (duration > 0) {
            updateDuration(duration);
        }
    }

    private void updateDuration(long duration) {
        binding.seekBar.setMax((int) duration);
        binding.txtTotalTime.setText(formatTime(duration));
    }

    private void startProgressUpdate() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (!isUserSeeking && playerManager != null) {
                    long position = playerManager.getPlayer().getCurrentPosition();
                    binding.seekBar.setProgress((int) position);
                    binding.txtCurrentTime.setText(formatTime(position));

                    // Update duration if it wasn't available before
                    long duration = playerManager.getDuration();
                    if (duration > 0 && binding.seekBar.getMax() != duration) {
                        updateDuration(duration);
                    }
                }
                handler.postDelayed(this, UPDATE_INTERVAL);
            }
        });
    }

    private String formatTime(long timeMs) {
        long seconds = (timeMs / 1000) % 60;
        long minutes = (timeMs / (1000 * 60)) % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        binding = null;
    }
} 