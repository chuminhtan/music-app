/**
 * TRANG SEARCH
 * */


package com.myteam.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;



import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.myteam.myapplication.R;
import com.myteam.myapplication.activity.PlayMusicActivity;
import com.myteam.myapplication.adapter.ListSongBySearchAdapter;
import com.myteam.myapplication.data.SongData;
import com.myteam.myapplication.data.SongListAsyncResponse;
import com.myteam.myapplication.model.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    View view;
    ListView lv_song;
    SearchView search_view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        lv_song = v.findViewById(R.id.lv_song);
        search_view = v.findViewById(R.id.search_field);


        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchDatabase(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        lv_song.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Song songModel = (Song) parent.getItemAtPosition(position);
                Bundle sendBundle = new Bundle();
                Intent intent = new Intent(getContext(), PlayMusicActivity.class);
                sendBundle.putInt("song_id", songModel.getId());
                sendBundle.putString("song_name", songModel.getName());
                sendBundle.putString("url", songModel.getSrc());
                sendBundle.putString("src_img", songModel.getImg());
                //sendBundle.putString("artist_name", songModel.getArtist().getName());
                intent.putExtras(sendBundle);
                startActivity(intent);
            }
        });

        return v;
    }

    private void searchDatabase(String query) {
        SongData songDataService = new SongData();
        songDataService.getListSongSimilar(new SongListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Song> songList) {
                ListSongBySearchAdapter listSongBySearchAdapter = new ListSongBySearchAdapter(getActivity(),R.layout.fragment_search,songList);
                lv_song.setAdapter(listSongBySearchAdapter);
            }
        },query);

    }
}