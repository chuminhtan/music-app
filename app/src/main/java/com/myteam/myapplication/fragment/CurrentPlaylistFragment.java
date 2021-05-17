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
import com.myteam.myapplication.activity.PlayMusicActivity;
import com.myteam.myapplication.adapter.SonglistAdapter;

public class CurrentPlaylistFragment extends Fragment {
    View view;
    SonglistAdapter songlistAdapter;

    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_current_playlist, container, false);


        recyclerView = view.findViewById(R.id.recyclerview_current_playlist);

        if (PlayMusicActivity.songList.size() > 0) {
            songlistAdapter = new SonglistAdapter(getActivity(), R.layout.songlist_item, PlayActivity.SONGLIST);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(songlistAdapter);
        }

        return view;
    }
}
