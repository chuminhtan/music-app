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
import com.myteam.myapplication.adapter.ArtistAdapter;
import com.myteam.myapplication.adapter.SonglistAdapter;
import com.myteam.myapplication.data.ArtistAsyncRespone;
import com.myteam.myapplication.data.ArtistData;
import com.myteam.myapplication.data.ArtistListAsyncRespone;
import com.myteam.myapplication.data.SongData;
import com.myteam.myapplication.data.SongListAsyncResponse;
import com.myteam.myapplication.model.Artist;
import com.myteam.myapplication.model.Song;

import java.util.ArrayList;
import java.util.List;

public class ArtistFragment extends Fragment {

    private RecyclerView lvArtistSearchResult;
    private View view;
    private ArtistAdapter artistAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_artist, container, false);
        mapping();
        getData();
        return view;
    }

    public void mapping() {
        // Recyclerview
        lvArtistSearchResult = (RecyclerView) view.findViewById(R.id.lv_artist_search_results);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
        lvArtistSearchResult.setLayoutManager(mLayoutManager);
    }

    // Get data
    public void getData() {
        Log.d("WORD", SearchFragment.WORD);
        new ArtistData().getArtistSimilar(new ArtistListAsyncRespone(){
            @Override
            public void processFinished(ArrayList<Artist> ArtistList) {
                artistAdapter = new ArtistAdapter(getActivity(), R.layout.artist_item, ArtistList);
                lvArtistSearchResult.setAdapter(artistAdapter);
            }
        }, SearchFragment.WORD);
    }
}