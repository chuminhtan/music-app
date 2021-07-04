package com.myteam.myapplication.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myteam.myapplication.R;
import com.myteam.myapplication.adapter.PlaylistSquareAdapter;
import com.myteam.myapplication.adapter.SonglistAdapter;
import com.myteam.myapplication.data.PlaylistArrayListAsyncResponse;
import com.myteam.myapplication.data.PlaylistData;
import com.myteam.myapplication.data.SongData;
import com.myteam.myapplication.data.SongListAsyncResponse;
import com.myteam.myapplication.model.Playlist;
import com.myteam.myapplication.model.Song;

import java.util.ArrayList;

public class PlaylistsFragment extends Fragment {

    private RecyclerView lvPlaylistSearchResult;
    private View view;
    private PlaylistSquareAdapter playlistSquareAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_playlists, container, false);
        mapping();
        getData();
        return view;
    }

    public void mapping() {
        // Recyclerview
        lvPlaylistSearchResult= (RecyclerView) view.findViewById(R.id.lv_playlist_search_results);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(),3);
        lvPlaylistSearchResult.setLayoutManager(mLayoutManager);
    }

    // Get data
    public void getData() {
        Log.d("WORD", SearchFragment.WORD);
        new PlaylistData().getPlaylistsSimilar(new PlaylistArrayListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Playlist> playlistArrayList) {
                playlistSquareAdapter = new PlaylistSquareAdapter(getActivity(), R.layout.playlist_square_item, playlistArrayList);
                lvPlaylistSearchResult.setAdapter(playlistSquareAdapter);
            }
        }, SearchFragment.WORD);
    }
}