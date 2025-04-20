package com.example.midnightmusic.ui.library;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midnightmusic.R;
import com.example.midnightmusic.models.Album;
import com.example.midnightmusic.ui.adapters.AlbumCardAdapter;

import java.util.ArrayList;
import java.util.List;

public class PlaylistsFragment extends Fragment implements AlbumCardAdapter.OnAlbumClickListener {
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_library_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler_view);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        List<Album> playlists = getPlaylists();
        AlbumCardAdapter adapter = new AlbumCardAdapter(playlists, this);
        recyclerView.setAdapter(adapter);
    }

    private List<Album> getPlaylists() {
        List<Album> playlists = new ArrayList<>();
        
        // Add sample playlists
        playlists.add(new Album("1", "Liked Songs", "1234 songs", null, "playlist"));
        playlists.add(new Album("2", "Discover Weekly", "Updated every Monday", null, "playlist"));
        playlists.add(new Album("3", "Release Radar", "Updated every Friday", null, "playlist"));
        playlists.add(new Album("4", "Daily Mix 1", "Based on your listening", null, "playlist"));
        playlists.add(new Album("5", "Daily Mix 2", "Based on your listening", null, "playlist"));
        playlists.add(new Album("6", "Daily Mix 3", "Based on your listening", null, "playlist"));
        playlists.add(new Album("7", "Summer Hits", "Your summer playlist", null, "playlist"));
        playlists.add(new Album("8", "Workout Mix", "High energy tracks", null, "playlist"));
        
        return playlists;
    }

    @Override
    public void onAlbumClick(Album album) {
        // Handle playlist click
        Toast.makeText(requireContext(), 
                      "Selected playlist: " + album.getTitle(), 
                      Toast.LENGTH_SHORT).show();
    }
} 