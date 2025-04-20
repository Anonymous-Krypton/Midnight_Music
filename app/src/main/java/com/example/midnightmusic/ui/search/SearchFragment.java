package com.example.midnightmusic.ui.search;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midnightmusic.databinding.FragmentSearchBinding;
import com.example.midnightmusic.models.Genre;
import com.example.midnightmusic.ui.adapters.GenreAdapter;
import com.example.midnightmusic.data.model.Song;
import com.example.midnightmusic.data.network.JioSaavnService;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchFragment extends Fragment implements GenreAdapter.OnGenreClickListener {
    private FragmentSearchBinding binding;
    private SearchAdapter searchAdapter;
    private GenreAdapter genreAdapter;
    private JioSaavnService api;
    private RecyclerView.LayoutManager searchLayoutManager;
    private RecyclerView.LayoutManager genreLayoutManager;
    private boolean isShowingGenres = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        setupRetrofit();
        return binding.getRoot();
    }

    private void setupRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(JioSaavnService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(JioSaavnService.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupLayoutManagers();
        setupSearchInput();
        setupRecyclerView();
        setupGenresGrid();
        showGenresView();
    }

    private void setupLayoutManagers() {
        searchLayoutManager = new LinearLayoutManager(requireContext());
        genreLayoutManager = new GridLayoutManager(requireContext(), 2);
    }

    private void setupSearchInput() {
        TextInputEditText searchInput = binding.searchInput;
        searchInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                String query = searchInput.getText().toString().trim();
                if (query.isEmpty()) {
                    showGenresView();
                } else {
                    performSearch(query);
                }
                return true;
            }
            return false;
        });

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().isEmpty()) {
                    showGenresView();
                }
            }
        });
    }

    private void setupRecyclerView() {
        searchAdapter = new SearchAdapter(new SearchAdapter.SearchAdapterListener() {
            @Override
            public void onSongClick(Song song) {
                Toast.makeText(requireContext(), "Playing: " + song.getSong(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPlayNow(Song song) {
                Toast.makeText(requireContext(), "Playing now: " + song.getSong(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAddToPlaylist(Song song) {
                Toast.makeText(requireContext(), "Add to playlist: " + song.getSong(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onQueueNext(Song song) {
                Toast.makeText(requireContext(), "Queued next: " + song.getSong(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onToggleLike(Song song) {
                Toast.makeText(requireContext(), "Toggled like for: " + song.getSong(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void performSearch(String query) {
        if (query == null || query.trim().isEmpty()) {
            showGenresView();
            return;
        }

        showLoadingState();
        
        api.searchSongs(query.trim(), true).enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(@NonNull Call<List<Song>> call, @NonNull Response<List<Song>> response) {
                if (!isAdded()) return;
                
                if (response.isSuccessful() && response.body() != null) {
                    List<Song> songs = response.body();
                    if (songs.isEmpty()) {
                        showEmptyState();
                    } else {
                        showSearchResults(songs);
                    }
                } else {
                    showError("Failed to get search results");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Song>> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                showError("Network error: " + t.getMessage());
            }
        });
    }

    private void showLoadingState() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.searchResultsRecycler.setVisibility(View.GONE);
        binding.emptyState.setVisibility(View.GONE);
    }

    private void showSearchResults(List<Song> songs) {
        if (isShowingGenres) {
            isShowingGenres = false;
            binding.searchResultsRecycler.setLayoutManager(searchLayoutManager);
            binding.searchResultsRecycler.setAdapter(searchAdapter);
        }
        
        binding.progressBar.setVisibility(View.GONE);
        binding.emptyState.setVisibility(View.GONE);
        binding.searchResultsRecycler.setVisibility(View.VISIBLE);
        searchAdapter.submitList(songs);
    }

    private void showEmptyState() {
        binding.progressBar.setVisibility(View.GONE);
        binding.searchResultsRecycler.setVisibility(View.GONE);
        binding.emptyState.setVisibility(View.VISIBLE);
        binding.emptyState.setText("No results found");
    }

    private void showError(String message) {
        binding.progressBar.setVisibility(View.GONE);
        binding.searchResultsRecycler.setVisibility(View.GONE);
        binding.emptyState.setVisibility(View.VISIBLE);
        binding.emptyState.setText(message);
    }

    private void showGenresView() {
        if (!isShowingGenres) {
            isShowingGenres = true;
            binding.searchResultsRecycler.setLayoutManager(genreLayoutManager);
            binding.searchResultsRecycler.setAdapter(genreAdapter);
        }
        
        binding.progressBar.setVisibility(View.GONE);
        binding.emptyState.setVisibility(View.GONE);
        binding.searchResultsRecycler.setVisibility(View.VISIBLE);
    }

    private void setupGenresGrid() {
        List<Genre> genres = getGenres();
        genreAdapter = new GenreAdapter(genres, this);
    }

    private List<Genre> getGenres() {
        List<Genre> genres = new ArrayList<>();
        
        genres.add(new Genre("1", "Pop", null, Color.parseColor("#FF1DB954")));
        genres.add(new Genre("2", "Rock", null, Color.parseColor("#FF1E3264")));
        genres.add(new Genre("3", "Hip-Hop", null, Color.parseColor("#FF7358FF")));
        genres.add(new Genre("4", "Jazz", null, Color.parseColor("#FFE8115B")));
        genres.add(new Genre("5", "Electronic", null, Color.parseColor("#FF148A08")));
        genres.add(new Genre("6", "Classical", null, Color.parseColor("#FF8400E7")));
        genres.add(new Genre("7", "R&B", null, Color.parseColor("#FFE91429")));
        genres.add(new Genre("8", "Metal", null, Color.parseColor("#FF777777")));
        genres.add(new Genre("9", "Folk", null, Color.parseColor("#FF537AA1")));
        genres.add(new Genre("10", "Latin", null, Color.parseColor("#FFBC5900")));
        genres.add(new Genre("11", "Indie", null, Color.parseColor("#FF006450")));
        genres.add(new Genre("12", "Podcasts", null, Color.parseColor("#FF8C1932")));
        
        return genres;
    }

    @Override
    public void onGenreClick(Genre genre) {
        performSearch(genre.getName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
} 