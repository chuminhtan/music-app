package com.myteam.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.myteam.myapplication.R;
import com.myteam.myapplication.adapter.SonglistAdapter;
import com.myteam.myapplication.data.AlbumSongAsyncRespone;
import com.myteam.myapplication.data.AlbumSongData;
import com.myteam.myapplication.model.Album;
import com.myteam.myapplication.model.AlbumSong;
import com.myteam.myapplication.model.Song;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AlbumDetailActivity extends AppCompatActivity implements View.OnClickListener{
    private Album albumIntent;
    private AlbumSong mAlbumSong;
    private ImageView imageAlbum;
    private Button btnPlay;
    private Toolbar toolbar;

    private CoordinatorLayout coordinatorLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView recyclerViewSonglist;
    private SonglistAdapter songlistAdapter;

    ArrayList<Song> songList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);

        mapping();
        getDataIntent();
        init();

        // Check album is null
        if(albumIntent != null && !albumIntent.getName().equals("")) {
            setInfoAlbum(albumIntent.getName(), albumIntent.getImageUrl());
            getSonglist(albumIntent.getId());
        }

        btnPlay.setOnClickListener(this);
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
        collapsingToolbarLayout.setTitle(albumIntent.getName());
    }

    private void mapping() {
        coordinatorLayout = findViewById(R.id.coordinator_album_detail);
        collapsingToolbarLayout = findViewById(R.id.collapsingtoolbarlayout_album_detail);
        imageAlbum = findViewById(R.id.imageview_album_detail);
        btnPlay = findViewById(R.id.button_play_album_detail);
        toolbar = findViewById(R.id.toolbar_album_detail);

        // Thiết lập recyclerView hiển thị hàng dọc
        recyclerViewSonglist = findViewById(R.id.recyclerview_songlist_album_detail);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(AlbumDetailActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerViewSonglist.setLayoutManager(mLayoutManager);
    }

    private void setInfoAlbum(String albumName, String albumImageUrl) {
        Picasso.with(this).load(albumIntent.getImageUrl()).into(imageAlbum);
    }

    private void getDataIntent() {
        Intent intent = getIntent();

        if (intent != null) {

            // Data : Album
            if (intent.hasExtra("album")) {
                albumIntent = (Album) intent.getSerializableExtra("album");
                Toast.makeText(AlbumDetailActivity.this, albumIntent.getName(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getSonglist(int albumId) {
        mAlbumSong = new AlbumSong();

        new AlbumSongData().getAlbumSongByAlbumId(albumId, new AlbumSongAsyncRespone() {
            @Override
            public void processFinished(AlbumSong albumSong) {
                mAlbumSong = albumSong;
                songList = albumSong.getSongs();
                songlistAdapter = new SonglistAdapter(AlbumDetailActivity.this, R.layout.songlist_item, songList);
                recyclerViewSonglist.setAdapter(songlistAdapter);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_play_album_detail:
                Intent intent = new Intent(AlbumDetailActivity.this, PlayActivity.class);
                intent.putExtra("SONGLIST", songList);
                AlbumDetailActivity.this.startActivity(intent);
        }
    }
}
