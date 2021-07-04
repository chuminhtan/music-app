package com.myteam.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.myteam.myapplication.R;
import com.myteam.myapplication.adapter.ArtistAdapter;
import com.myteam.myapplication.adapter.SonglistAdapter;
import com.myteam.myapplication.data.CollectionAsyncResponse;
import com.myteam.myapplication.data.CollectionData;
import com.myteam.myapplication.data.UserAsyncResponse;
import com.myteam.myapplication.data.UserData;
import com.myteam.myapplication.model.Artist;
import com.myteam.myapplication.model.Collection;
import com.myteam.myapplication.model.Playlist;
import com.myteam.myapplication.model.Song;
import com.myteam.myapplication.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PlaylistDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private Playlist playlistIntent;
    private Collection mCollection;
    private ArrayList<Artist> mArtists;
    private ImageView imagePlaylist, imgLikePlaylist;
    private Button btnPlay;
    private Toolbar toolbar;

    private CoordinatorLayout coordinatorLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView recyclerViewSonglist;
    private RecyclerView recyclerViewArtist;
    private SonglistAdapter songlistAdapter;
    private ArtistAdapter artistAdapter;
    private User user;
    private ArrayList<Song> songList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_detail);
        getUser();
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

        btnPlay.setOnClickListener(this);
        imgLikePlaylist.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setButtonLike();
    }

    private void getSonglist(int playlistId) {
        mCollection = new Collection();
        mArtists = new ArrayList<>();

        new CollectionData().getCollectionByPlaylistId(playlistId, new CollectionAsyncResponse() {
            @Override
            public void processFinished(Collection collection, ArrayList<Artist> artists) {
                mCollection = collection;
                mArtists = artists;
                songList = collection.getSongs();
                songlistAdapter = new SonglistAdapter(PlaylistDetailActivity.this, R.layout.songlist_item, songList);

                recyclerViewSonglist.setAdapter(songlistAdapter);
                artistAdapter = new ArtistAdapter(PlaylistDetailActivity.this, R.layout.artist_item, artists);
                recyclerViewArtist.setAdapter(artistAdapter);
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
        imgLikePlaylist = findViewById(R.id.button_like_playlist);

        // Thiết lập recyclerView hiển thị hàng dọc
        recyclerViewSonglist = findViewById(R.id.recyclerview_songlist_playlist_detail);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(PlaylistDetailActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerViewSonglist.setLayoutManager(mLayoutManager);

        // Thiết lập recyclerView hiển thị hàng ngang
        recyclerViewArtist = findViewById(R.id.recyclerview_playlist_artists);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(PlaylistDetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewArtist.setLayoutManager(layoutManager2);
    }

    private void getDataIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            // Data : Playlist
            if (intent.hasExtra("playlist")) {
                playlistIntent = (Playlist) intent.getSerializableExtra("playlist");
            }
        }
    }

    // EVENT
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_play_playlist_detail:
                Intent intent = new Intent(PlaylistDetailActivity.this, PlayActivity.class);
                intent.putExtra("SONGLIST", songList);
                PlaylistDetailActivity.this.startActivity(intent);
                break;
            case R.id.button_like_playlist:
                setLikePlaylist();
        }
    }

    private void getUser() {
        SharedPreferences sharedPref = PlaylistDetailActivity.this.getSharedPreferences("USER", Context.MODE_PRIVATE);
        user = new User();
        user.setId(sharedPref.getInt("user_id", 0));
        user.setName(sharedPref.getString("user_name", ""));
        user.setEmail(sharedPref.getString("user_email", ""));
    }


    private void setButtonLike() {
        if (user.getId() == 0) {
            imgLikePlaylist.setVisibility(View.GONE);
        } else {
            imgLikePlaylist.setVisibility(View.VISIBLE);
            checkLikePlaylist();
        }
    }

    private void setLikePlaylist() {
        String type = "playlist";
        new UserData().setLikePlaylist(user.getId(), playlistIntent.getId(), type, new UserAsyncResponse() {
            @Override
            public void processFinished(String result) {
                if (result.equalsIgnoreCase("like")) {
                    Toast.makeText(PlaylistDetailActivity.this, "Đã Yêu Thích", Toast.LENGTH_SHORT).show();
                    imgLikePlaylist.setImageResource(R.drawable.ic_baseline_favorite_50);
                } else {
                    Toast.makeText(PlaylistDetailActivity.this, "Hủy Yêu Thích", Toast.LENGTH_SHORT).show();
                    imgLikePlaylist.setImageResource(R.drawable.ic_baseline_favorite_border_50);
                }
            }
        });
    }

    private void checkLikePlaylist() {
        String type = "playlist";
        new UserData().checkLiked(user.getId(), playlistIntent.getId(), type, new UserAsyncResponse() {
            @Override
            public void processFinished(String result) {
                if (result.equalsIgnoreCase("yes")) {
                    imgLikePlaylist.setImageResource(R.drawable.ic_baseline_favorite_50);
                } else {
                    imgLikePlaylist.setImageResource(R.drawable.ic_baseline_favorite_border_50);
                }
            }
        });
    }
}