package com.example.midnightmusic.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midnightmusic.databinding.FragmentHomeBinding;
import com.example.midnightmusic.models.Album;
import com.example.midnightmusic.ui.adapters.AlbumCardAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements AlbumCardAdapter.OnAlbumClickListener {
    private FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerViews();
        updateGreeting();
    }

    private void setupRecyclerViews() {
        setupRecentlyPlayed();
        setupMadeForYou();
        setupNewReleases();
    }

    private void setupRecentlyPlayed() {
        RecyclerView recyclerView = binding.recentlyPlayedRecycler;
        List<Album> recentlyPlayed = getRecentlyPlayedAlbums();
        AlbumCardAdapter adapter = new AlbumCardAdapter(recentlyPlayed, this);
        recyclerView.setAdapter(adapter);
    }

    private void setupMadeForYou() {
        RecyclerView recyclerView = binding.madeForYouRecycler;
        List<Album> madeForYou = getMadeForYouPlaylists();
        AlbumCardAdapter adapter = new AlbumCardAdapter(madeForYou, this);
        recyclerView.setAdapter(adapter);
    }

    private void setupNewReleases() {
        RecyclerView recyclerView = binding.newReleasesRecycler;
        List<Album> newReleases = getNewReleases();
        AlbumCardAdapter adapter = new AlbumCardAdapter(newReleases, this);
        recyclerView.setAdapter(adapter);
    }

    private List<Album> getRecentlyPlayedAlbums() {
        List<Album> albums = new ArrayList<>();
        // Add sample data
        albums.add(new Album("1", "Discovery", "Daft Punk", null, "album"));
        albums.add(new Album("2", "Random Access Memories", "Daft Punk", null, "album"));
        albums.add(new Album("3", "Homework", "Daft Punk", null, "album"));
        albums.add(new Album("4", "Human After All", "Daft Punk", null, "album"));
        return albums;
    }

    private List<Album> getMadeForYouPlaylists() {
        List<Album> playlists = new ArrayList<>();
        // Add sample data
        playlists.add(new Album("5", "Daily Mix 1", "Based on your listening", null, "playlist"));
        playlists.add(new Album("6", "Daily Mix 2", "Based on your listening", null, "playlist"));
        playlists.add(new Album("7", "Discover Weekly", "Updated every Monday", null, "playlist"));
        playlists.add(new Album("8", "Release Radar", "Updated every Friday", null, "playlist"));
        return playlists;
    }

    private List<Album> getNewReleases() {
        List<Album> albums = new ArrayList<>();
        // Add sample data
        albums.add(new Album("9", "New Album 1", "Artist 1", null, "album"));
        albums.add(new Album("10", "New Album 2", "Artist 2", null, "album"));
        albums.add(new Album("11", "New Album 3", "Artist 3", null, "album"));
        albums.add(new Album("12", "New Album 4", "Artist 4", null, "album"));
        return albums;
    }

    private void updateGreeting() {
        int hour = java.time.LocalTime.now().getHour();
        String greeting;
        
        if (hour >= 5 && hour < 12) {
            greeting = "Good morning";
        } else if (hour >= 12 && hour < 18) {
            greeting = "Good afternoon";
        } else {
            greeting = "Good evening";
        }
        
        binding.greetingText.setText(greeting);
    }

    @Override
    public void onAlbumClick(Album album) {
        // Handle album click
        Toast.makeText(requireContext(), 
                      "Clicked: " + album.getTitle(), 
                      Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
} 