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

public class TopPlaylistFragment extends Fragment {
    private View view;
    private TextView txtListTitle; // Tên chủ đề
    private PlaylistSquareAdapter playlistSquareAdapter; // Gắn view và đổ dữ liệu
    private RecyclerView recyclerView;
    TextView tvShowMore;

    // Khởi tạo view Fragment
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_top_playlist, container, false);
        mapping();
        txtListTitle.setText("Top 100");
        getData();
        tvShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MorePlaylistActivity.class);
                intent.putExtra("type", 2);
                startActivity(intent);
            }
        });

        return view;
    }

    // Gắn thành phần bên xml vào biến
    public void mapping() {
        // Recyclerview
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_playlist_horizontal);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        // TextView
        txtListTitle = view.findViewById(R.id.textview_title_playlist_horizontal);

        tvShowMore = view.findViewById(R.id.textview_more_top_playlist);
    }

    // Get data
    public void getData() {
        // Get Playlists Type 1
        new PlaylistData().getPlaylistsType(2,4, new PlaylistArrayListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Playlist> playlistArrayList) {
                playlistSquareAdapter = new PlaylistSquareAdapter(getActivity(),R.layout.playlist_square_item,playlistArrayList);

                recyclerView.setAdapter(playlistSquareAdapter);
            }
        });
    }


}
