package com.myteam.myapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.myteam.myapplication.adapter.ArtistAdapter;
import com.myteam.myapplication.adapter.SonglistAdapter;
import com.myteam.myapplication.data.AlbumSongAsyncRespone;
import com.myteam.myapplication.data.AlbumSongData;
import com.myteam.myapplication.data.UserAsyncResponse;
import com.myteam.myapplication.data.UserData;
import com.myteam.myapplication.model.Album;
import com.myteam.myapplication.model.AlbumSong;
import com.myteam.myapplication.model.Artist;
import com.myteam.myapplication.model.Song;
import com.myteam.myapplication.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AlbumDetailActivity extends AppCompatActivity implements View.OnClickListener{
    private Album albumIntent;
    private AlbumSong mAlbumSong;
    private ArrayList<Artist> martists;
    private ImageView imageAlbum, imvLikeAlbum;
    private Button btnPlay;
    private Toolbar toolbar;

    private CoordinatorLayout coordinatorLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView recyclerViewSonglist;
    private RecyclerView recyclerViewArtist;
    private SonglistAdapter songlistAdapter;
    private ArtistAdapter artistAdapter;
    private User user;
    ArrayList<Song> songList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);

        getUser();
        mapping();
        getDataIntent();
        init();

        // Check album is null
        if(albumIntent != null && !albumIntent.getName().equals("")) {
            setInfoAlbum(albumIntent.getName(), albumIntent.getImageUrl());
            getSonglist(albumIntent.getId());
        }

        btnPlay.setOnClickListener(this);
        imvLikeAlbum.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setButtonLike();
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
        imvLikeAlbum = findViewById(R.id.button_like_album);
        btnPlay = findViewById(R.id.button_play_album_detail);
        toolbar = findViewById(R.id.toolbar_album_detail);

        // Thiết lập recyclerView hiển thị hàng dọc
        recyclerViewSonglist = findViewById(R.id.recyclerview_songlist_album_detail);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(AlbumDetailActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerViewSonglist.setLayoutManager(mLayoutManager);

        // Thiết lập recyclerView hiển thị hàng ngang
        recyclerViewArtist = findViewById(R.id.recyclerview_album_artists);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(AlbumDetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewArtist.setLayoutManager(layoutManager2);
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
            }
        }
    }

    private void getSonglist(int albumId) {
        mAlbumSong = new AlbumSong();
        martists = new ArrayList<>();

        new AlbumSongData().getAlbumSongByAlbumId(albumId, new AlbumSongAsyncRespone() {
            @Override
            public void processFinished(AlbumSong albumSong, ArrayList<Artist> artists) {
                mAlbumSong = albumSong;
                martists = artists;
                songList = albumSong.getSongs();

                songlistAdapter = new SonglistAdapter(AlbumDetailActivity.this, R.layout.songlist_item, songList);
                recyclerViewSonglist.setAdapter(songlistAdapter);

                artistAdapter = new ArtistAdapter(AlbumDetailActivity.this, R.layout.artist_item, martists);
                recyclerViewArtist.setAdapter(artistAdapter);
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
                break;
            case R.id.button_like_album:
                setLikeAlbum();
        }
    }

    private void getUser() {
        SharedPreferences sharedPref = AlbumDetailActivity.this.getSharedPreferences("USER", Context.MODE_PRIVATE);
        user = new User();
        user.setId(sharedPref.getInt("user_id", 0));
        user.setName(sharedPref.getString("user_name", ""));
        user.setEmail(sharedPref.getString("user_email", ""));
    }


    private void setButtonLike() {
        if (user.getId() == 0) {
            imvLikeAlbum.setVisibility(View.GONE);
        } else {
            imvLikeAlbum.setVisibility(View.VISIBLE);
            checkLikeAlbum();
        }
    }

    private void setLikeAlbum() {
        String type = "album";
        new UserData().setLikePlaylist(user.getId(), albumIntent.getId(), type, new UserAsyncResponse() {
            @Override
            public void processFinished(String result) {
                if (result.equalsIgnoreCase("like")) {
                    Toast.makeText(AlbumDetailActivity.this, "Đã Yêu Thích", Toast.LENGTH_SHORT).show();
                    imvLikeAlbum.setImageResource(R.drawable.ic_baseline_favorite_50);
                } else {
                    Toast.makeText(AlbumDetailActivity.this, "Hủy Yêu Thích", Toast.LENGTH_SHORT).show();
                    imvLikeAlbum.setImageResource(R.drawable.ic_baseline_favorite_border_50);
                }
            }
        });
    }

    private void checkLikeAlbum() {
        String type = "album";
        new UserData().checkLiked(user.getId(), albumIntent.getId(), type, new UserAsyncResponse() {
            @Override
            public void processFinished(String result) {
                if (result.equalsIgnoreCase("yes")) {
                    imvLikeAlbum.setImageResource(R.drawable.ic_baseline_favorite_50);
                } else {
                    imvLikeAlbum.setImageResource(R.drawable.ic_baseline_favorite_border_50);
                }
            }
        });
    }
}
