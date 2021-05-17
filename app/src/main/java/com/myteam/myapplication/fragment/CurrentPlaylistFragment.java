package com.myteam.myapplication.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myteam.myapplication.R;
import com.myteam.myapplication.activity.PlayActivity;
import com.myteam.myapplication.adapter.CurrentPlaylistAdapter;
import com.myteam.myapplication.adapter.SonglistAdapter;
import com.myteam.myapplication.model.Song;

import java.util.ArrayList;

public class CurrentPlaylistFragment extends Fragment {
    View view;
    CurrentPlaylistAdapter currentPlaylistAdapter;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_current_playlist, container, false);

        recyclerView = view.findViewById(R.id.recyclerview_current_playlist);

        if (PlayActivity.SONGLIST.size() > 0) {
            currentPlaylistAdapter = new CurrentPlaylistAdapter(getActivity(), R.layout.current_playlist_song_item, PlayActivity.SONGLIST);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(currentPlaylistAdapter);
        }
        return view;
    }

    public void updateCurrentPlaylistView(ArrayList<Song> songs) {
        currentPlaylistAdapter.updateList(songs);
    }

    public boolean isCreated() {
        if (view == null) {
            return false;
        }
        return true;
    }

}
