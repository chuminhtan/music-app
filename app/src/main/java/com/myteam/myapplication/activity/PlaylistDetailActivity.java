package com.myteam.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.myteam.myapplication.R;
import com.myteam.myapplication.adapter.SonglistAdapter;
import com.myteam.myapplication.data.CollectionAsyncResponse;
import com.myteam.myapplication.data.CollectionData;
import com.myteam.myapplication.data.PlaylistData;
import com.myteam.myapplication.model.Collection;
import com.myteam.myapplication.model.Playlist;
import com.myteam.myapplication.model.Song;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class PlaylistDetailActivity extends AppCompatActivity {
    private Playlist playlistIntent;
    private Collection mCollection;
    private ImageView imagePlaylist;
    private Button btnPlay;
    private Toolbar toolbar;

    private CoordinatorLayout coordinatorLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView recyclerViewSonglist;
    private SonglistAdapter songlistAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_detail);
        // Mapping
        mapping();

        // Data Intent
        getDataIntent();

        // Init
        init();

        // Check playlist is null
        if (playlistIntent != null && !playlistIntent.getName().equals("")) {
            setInfoPlaylist(playlistIntent.getName(), playlistIntent.getImageUrl());
            getSonglist(playlistIntent.getId());
        }
    }

    private void getSonglist(int playlistId) {
        mCollection = new Collection();

        new CollectionData().getCollectionByPlaylistId(playlistId, new CollectionAsyncResponse() {
            @Override
            public void processFinished(Collection collection) {
                mCollection = collection;

                ArrayList<Song> songs = collection.getSongs();

                songlistAdapter = new SonglistAdapter(getApplication(), R.layout.songlist_item, songs);
                recyclerViewSonglist.setAdapter(songlistAdapter);

                Log.d("COLLECTION", collection.toString());
            }
        });
    }

    private void setInfoPlaylist(String playlistName, String playlistImageUrl) {
        Picasso.with(this).load(playlistIntent.getImageUrl()).into(imagePlaylist);
    }

    private void init() {
        // Set Toolbar
        setSupportActionBar(toolbar);
        // Tạo mũi tên back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Set sự kiện khi click
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Set toolbar title
        collapsingToolbarLayout.setTitle(playlistIntent.getName());

    }

    private void mapping() {
        coordinatorLayout = findViewById(R.id.coordinator_playlist_detail);
        collapsingToolbarLayout = findViewById(R.id.collapsingtoolbarlayout_playlist_detail);
        imagePlaylist = findViewById(R.id.imageview_playlist_detail);
        btnPlay = findViewById(R.id.button_play_playlist_detail);
        toolbar = findViewById(R.id.toolbar_playlist_detail);

        // Thiết lập recyclerView hiển thị hàng dọc
        recyclerViewSonglist = findViewById(R.id.recyclerview_songlist_playlist_detail);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewSonglist.setLayoutManager(mLayoutManager);

    }

    private void getDataIntent() {
        Intent intent = getIntent();

        if (intent != null) {

            // Data : Playlist
            if (intent.hasExtra("playlist")) {
                playlistIntent = (Playlist) intent.getSerializableExtra("playlist");
                Toast.makeText(this, playlistIntent.getName(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}