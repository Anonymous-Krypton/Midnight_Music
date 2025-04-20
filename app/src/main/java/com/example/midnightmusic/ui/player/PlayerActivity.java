package com.example.midnightmusic.ui.player;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.midnightmusic.databinding.ActivityPlayerBinding;

public class PlayerActivity extends AppCompatActivity {
    private ActivityPlayerBinding binding;
    private boolean isPlaying = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupClickListeners();
        setupSeekBar();
    }

    private void setupClickListeners() {
        // Collapse button
        binding.collapseButton.setOnClickListener(v -> finish());

        // More options button
        binding.moreButton.setOnClickListener(v -> {
            Toast.makeText(this, "More options", Toast.LENGTH_SHORT).show();
        });

        // Play/Pause button
        binding.playPauseButton.setOnClickListener(v -> {
            isPlaying = !isPlaying;
            updatePlayPauseButton();
            String message = isPlaying ? "Playing" : "Paused";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });

        // Previous button
        binding.previousButton.setOnClickListener(v -> {
            Toast.makeText(this, "Previous track", Toast.LENGTH_SHORT).show();
        });

        // Next button
        binding.nextButton.setOnClickListener(v -> {
            Toast.makeText(this, "Next track", Toast.LENGTH_SHORT).show();
        });

        // Shuffle button
        binding.shuffleButton.setOnClickListener(v -> {
            Toast.makeText(this, "Shuffle", Toast.LENGTH_SHORT).show();
        });

        // Repeat button
        binding.repeatButton.setOnClickListener(v -> {
            Toast.makeText(this, "Repeat", Toast.LENGTH_SHORT).show();
        });

        // Like button
        binding.likeButton.setOnClickListener(v -> {
            Toast.makeText(this, "Added to Liked Songs", Toast.LENGTH_SHORT).show();
        });

        // Queue button
        binding.queueButton.setOnClickListener(v -> {
            Toast.makeText(this, "Queue", Toast.LENGTH_SHORT).show();
        });

        // Devices button
        binding.devicesButton.setOnClickListener(v -> {
            Toast.makeText(this, "Available devices", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupSeekBar() {
        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    // TODO: Seek to position
                    updateTimeLabels(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Not needed
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Not needed
            }
        });

        // Set initial progress
        binding.seekBar.setMax(210); // 3:30 in seconds
        binding.seekBar.setProgress(0);
        updateTimeLabels(0);
    }

    private void updateTimeLabels(int progress) {
        binding.currentTime.setText(formatTime(progress));
        binding.totalTime.setText(formatTime(binding.seekBar.getMax()));
    }

    private void updatePlayPauseButton() {
        // TODO: Update play/pause icon based on isPlaying state
    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%d:%02d", minutes, remainingSeconds);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
} 