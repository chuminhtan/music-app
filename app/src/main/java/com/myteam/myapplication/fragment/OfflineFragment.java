package com.myteam.myapplication.fragment;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.myteam.myapplication.R;
import com.myteam.myapplication.activity.ArtistActivity;
import com.myteam.myapplication.activity.MorePlaylistActivity;
import com.myteam.myapplication.activity.PlayActivity;
import com.myteam.myapplication.adapter.SonglistAdapter;
import com.myteam.myapplication.model.Artist;
import com.myteam.myapplication.model.Song;
import com.myteam.myapplication.util.ServerInfo;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class OfflineFragment extends Fragment {
    View view;
    RecyclerView listView;
    String [] items;
    ArrayList<Song> listSong;
    Button playAll;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_offline, container, false);
        listView = view.findViewById(R.id.recyclerview_songlist_detail);
        playAll = view.findViewById(R.id.button_play_playlist_detail);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(layoutManager);

        runtimePermission();

        playAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PlayActivity.class);
                intent.putExtra("SONGLIST", listSong);
                startActivity(intent);
            }
        });
        return view;
    }
    public void runtimePermission ()
    {
        Dexter.withContext(getActivity()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        displaySong();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    public ArrayList<File> findSong (File file)
    {
        ArrayList<File> arrayList = new ArrayList<>();

        File[] files = file.listFiles();
        if (files == null) {
            playAll.setVisibility(View.GONE);
        } else {
            playAll.setVisibility(View.VISIBLE);
            for (File singlefile : files)
            {
                if (singlefile.isDirectory() && !singlefile.isHidden())
                {
                    arrayList.addAll(findSong(singlefile));
                }
                else
                {
                    if (singlefile.getName().endsWith(".mp3") || singlefile.getName().endsWith(".wav"))
                    {
                        arrayList.add(singlefile);
                    }
                }
            }
        }

        return arrayList;
    }

    void displaySong ()
    {
        final ArrayList<File> mySongs = findSong(Environment.getExternalStorageDirectory());
        if (mySongs == null) {
            return;
        }
        listSong = new ArrayList<Song>();
        items = new String[mySongs.size()];
        Log.d("OFFLINE", "mySONGs: " + String.valueOf(mySongs.size()));
        for (int i=0; i<mySongs.size(); i++)
        {
            items[i] = mySongs.get(i).getName().toString().replace(".mp3", "").replace(".wav","");
            Song song = new Song();
            song.setUri(mySongs.get(i).toString());
            song.setName(mySongs.get(i).getName());
            ArrayList<Artist> artists = new ArrayList<>();
            Artist artist = new Artist();
            artist.setName("Unknown");
            artists.add(artist);
            song.setArtists(artists);
            song.setImg("music.png");
            song.setArtist(artist);
            listSong.add(song);
        }
        SonglistAdapter songlistAdapter = new SonglistAdapter(getActivity(), R.layout.songlist_item, listSong);
        listView.setAdapter(songlistAdapter);
    }

    /*
    * Song
    * Name
    * Artist : Unknown
    * Anh: Lay anh mac dinh
    * uri
    * Tao Intent
    * */
}