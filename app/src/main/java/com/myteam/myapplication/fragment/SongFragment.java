package com.myteam.myapplication.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
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

public class SongFragment extends Fragment {
    private RecyclerView lvSongSearchResult;
    private View view;
    private SonglistAdapter songlistAdapter;
    private String word;
    private ArrayList<Song> songArrayList;

    public SongFragment(){

    }
    public SongFragment(ArrayList<Song> songs){
        this.songArrayList = songs;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getData();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = new Bundle();
        word = bundle.getString("");
        view = inflater.inflate(R.layout.fragment_song, container, false);
        mapping();
        getData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    public void mapping() {
        // Recyclerview
        lvSongSearchResult = (RecyclerView) view.findViewById(R.id.lv_song_search_results);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        lvSongSearchResult.setLayoutManager(mLayoutManager);
    }

    // Get data
    public void getData() {
        Log.d("WORD", SearchFragment.WORD);
        new SongData().getListSongSimilar(new SongListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Song> songList) {
                songlistAdapter = new SonglistAdapter(getActivity(), R.layout.songlist_item, songList);
                lvSongSearchResult.setAdapter(songlistAdapter);
            }
        }, SearchFragment.WORD);
    }
}