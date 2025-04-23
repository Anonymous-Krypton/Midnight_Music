package com.example.midnightmusic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.bumptech.glide.Glide;
import com.example.midnightmusic.data.model.Song;
import com.example.midnightmusic.databinding.ActivityMainBinding;
import com.example.midnightmusic.player.MusicPlayerManager;
import com.example.midnightmusic.ui.player.PlayerActivity;
import android.app.ActivityOptions;
import android.util.Pair;
import android.view.animation.AnimationUtils;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MusicPlayerManager playerManager;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupNavigation();
        setupMiniPlayer();
    }

    private void setupNavigation() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.bottomNav, navController);
    }

    private void setupMiniPlayer() {
        playerManager = MusicPlayerManager.getInstance(this);

        // Handle click on mini player to open full player with slide up animation
        binding.miniPlayer.miniPlayerContainer.setOnClickListener(v -> {
            Intent intent = new Intent(this, PlayerActivity.class);
            
            // Create transition pairs for shared elements
            Pair<View, String> albumArt = Pair.create(
                binding.miniPlayer.imgMiniArt, 
                "transition_album_art"
            );
            Pair<View, String> songTitle = Pair.create(
                binding.miniPlayer.txtMiniTitle, 
                "transition_song_title"
            );
            Pair<View, String> artistName = Pair.create(
                binding.miniPlayer.txtMiniArtist, 
                "transition_artist_name"
            );
            Pair<View, String> playButton = Pair.create(
                binding.miniPlayer.btnMiniPlayPause,
                "transition_play_button"
            );

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                this, albumArt, songTitle, artistName, playButton
            );
            
            startActivity(intent, options.toBundle());
            overridePendingTransition(R.anim.slide_up, R.anim.stay);
        });

        // Set initial play/pause button state
        updatePlayPauseButton(playerManager.isPlaying());

        // Handle play/pause button
        binding.miniPlayer.btnMiniPlayPause.setOnClickListener(v -> {
            playerManager.togglePlayPause();
            v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click));
        });

        // Handle skip previous button
        binding.miniPlayer.btnMiniPrevious.setOnClickListener(v -> {
            playerManager.skipToPrevious();
            v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click));
        });

        // Handle skip next button
        binding.miniPlayer.btnMiniNext.setOnClickListener(v -> {
            playerManager.skipToNext();
            v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click));
        });

        // Observe current song
        playerManager.getCurrentSongLiveData().observe(this, this::updateMiniPlayer);
        
        // Observe playback state
        playerManager.getPlayingLiveData().observe(this, this::updatePlayPauseButton);

        // Observe progress
        playerManager.getCurrentPosition().observe(this, position -> {
            if (position != null && playerManager.getDuration() > 0) {
                int progress = (int) ((position * 100) / playerManager.getDuration());
                binding.miniPlayer.progressMini.setProgress(progress);
            }
        });

        // Show/hide mini player based on current song
        playerManager.getCurrentSongLiveData().observe(this, song -> {
            if (song != null && binding.miniPlayer.getRoot().getVisibility() != View.VISIBLE) {
                binding.miniPlayer.getRoot().setVisibility(View.VISIBLE);
                binding.miniPlayer.getRoot().startAnimation(
                    AnimationUtils.loadAnimation(this, R.anim.slide_up)
                );
            } else if (song == null) {
                binding.miniPlayer.getRoot().setVisibility(View.GONE);
            }
        });
    }

    private void updateMiniPlayer(Song song) {
        if (song == null) return;

        binding.miniPlayer.txtMiniTitle.setText(song.getSong());
        binding.miniPlayer.txtMiniArtist.setText(song.getSingers());
        
        Glide.with(this)
            .load(song.getImageUrl())
            .placeholder(R.drawable.placeholder_song)
            .into(binding.miniPlayer.imgMiniArt);

        // Update play/pause button state
        updatePlayPauseButton(playerManager.isPlaying());
    }

    private void updatePlayPauseButton(boolean isPlaying) {
        if (binding.miniPlayer.btnMiniPlayPause != null) {
            binding.miniPlayer.btnMiniPlayPause.setImageResource(
                isPlaying ? R.drawable.ic_pause : R.drawable.ic_play
            );
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
} 