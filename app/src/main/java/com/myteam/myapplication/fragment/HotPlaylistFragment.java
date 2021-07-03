package com.myteam.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myteam.myapplication.R;
import com.myteam.myapplication.activity.MorePlaylistActivity;
import com.myteam.myapplication.adapter.PlaylistSquareAdapter;
import com.myteam.myapplication.data.PlaylistArrayListAsyncResponse;
import com.myteam.myapplication.data.PlaylistData;
import com.myteam.myapplication.model.Playlist;

import java.util.ArrayList;

public class HotPlaylistFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    PlaylistSquareAdapter playlistSquareAdapter;
    ArrayList<Playlist> playlists;
    TextView tvShowMore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_hot_playlist, container, false);

        mapping();
        if (playlists == null) {
            getData();
        }

        // event
    tvShowMore.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), MorePlaylistActivity.class);
            intent.putExtra("type", 1);
            startActivity(intent);
        }
    });
        return view;
    }

    public void mapping() {
        // Recyclerview
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_playlist_horizontal);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);

        tvShowMore = view.findViewById(R.id.textview_more_hot_playlist);

    }

    private void getData() {
        // Get Playlists Type 1
        new PlaylistData().getPlaylistsType(1,4, new PlaylistArrayListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Playlist> playlistArrayList) {
                playlists = new ArrayList<>();
                playlists = playlistArrayList;
                playlistSquareAdapter = new PlaylistSquareAdapter(getActivity(), R.layout.playlist_square_item ,playlistArrayList);
                recyclerView.setAdapter(playlistSquareAdapter);
            }
        });
    }
















}
