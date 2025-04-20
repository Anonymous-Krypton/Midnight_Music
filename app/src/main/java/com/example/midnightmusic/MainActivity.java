package com.example.midnightmusic;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.navigation.fragment.NavHostFragment;

import com.example.midnightmusic.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
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
        // Configure the bottom navigation view with the nav controller
        BottomNavigationView navView = binding.bottomNavView;
        
        // Get the NavHostFragment and NavController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        
        // Set up the configuration for the bottom navigation
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,
                R.id.navigation_search,
                R.id.navigation_library,
                R.id.navigation_profile
        ).build();

        NavigationUI.setupWithNavController(navView, navController);
    }

    private void setupMiniPlayer() {
        // Set up click listeners for the mini player
        binding.miniPlayer.getRoot().setOnClickListener(v -> {
            // TODO: Launch full player activity
        });

        binding.miniPlayer.miniPlayerPlayPause.setOnClickListener(v -> {
            // TODO: Toggle play/pause
        });
    }
} 