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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myteam.myapplication.R;
import com.myteam.myapplication.activity.MorePlaylistActivity;
import com.myteam.myapplication.adapter.PlaylistRectangleAdapter;
import com.myteam.myapplication.data.PlaylistArrayListAsyncResponse;
import com.myteam.myapplication.data.PlaylistData;
import com.myteam.myapplication.model.Playlist;

import java.util.ArrayList;

public class ActivityPlaylistFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private PlaylistRectangleAdapter playlistRectangleAdapter;
    TextView tvShowMore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_activity_playlist, container, false);
        mapping();
        getData();

        tvShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MorePlaylistActivity.class);
                intent.putExtra("type", 3);
                startActivity(intent);
            }
        });
        return view;
    }

    // Gắn thành phần bên xml vào biến
    public void mapping() {
        // Recyclerview
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_playlist_grid);

        // Set Grid - hiển thị lưới cho từng item
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);

        tvShowMore = view.findViewById(R.id.textview_more_activity_playlist);
    }

    // Get data
    public void getData() {
        // Get Playlists Type 1
        new PlaylistData().getPlaylistsType(3,4, new PlaylistArrayListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Playlist> playlistArrayList) {
                playlistRectangleAdapter = new PlaylistRectangleAdapter(getActivity(),R.layout.playlist_rectangle_item,playlistArrayList);
                recyclerView.setAdapter(playlistRectangleAdapter);
            }
        });
    }
}
