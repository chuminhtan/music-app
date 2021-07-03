package com.myteam.myapplication.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.myteam.myapplication.R;
import com.myteam.myapplication.adapter.PlaylistAdapter;
import com.myteam.myapplication.data.CollectionAsyncResponse;
import com.myteam.myapplication.data.CollectionData;
import com.myteam.myapplication.data.PlaylistArrayListAsyncResponse;
import com.myteam.myapplication.data.PlaylistData;
import com.myteam.myapplication.model.Collection;
import com.myteam.myapplication.model.Playlist;

import java.util.ArrayList;

public class PlaylistFragment extends Fragment {

    View view;
    ListView lvPlaylist;
    TextView txtTitlePlaylist, txtViewMorePlaylist;
    PlaylistAdapter playlistAdapter;
    ArrayList<Playlist> playlistsType1;

    // chuyển đổi từ xml sang view cho java
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // có layout của xml
        view = inflater.inflate(R.layout.fragment_playlist, container, false);

        lvPlaylist = view.findViewById(R.id.listview_playlist);
        txtTitlePlaylist = view.findViewById(R.id.textview_title_playlist);
        txtViewMorePlaylist = view.findViewById(R.id.textview_view_more_playlist);

        // Lấy dữ liệu từ server
        getPlaylistsByType(1);

        return view;
    }

    private void getPlaylistsByType(int i) {
        playlistsType1 = new PlaylistData().getPlaylistsType(1,4, new PlaylistArrayListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Playlist> playlistArrayList) {
                // Adapter - Tạo view thành phần + gắn data vô return view
                playlistAdapter = new PlaylistAdapter(getActivity(), R.layout.playlist_dynamic, playlistArrayList);

                lvPlaylist.setAdapter(playlistAdapter);
            }

        });
    }

}
