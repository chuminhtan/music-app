package com.myteam.myapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.myteam.myapplication.R;
import com.myteam.myapplication.adapter.SonglistAdapter;
import com.myteam.myapplication.data.LikeSongAsyncResponse;
import com.myteam.myapplication.data.LikeSongData;
import com.myteam.myapplication.model.Song;
import com.myteam.myapplication.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LikedSongListActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView imageView;
    private Button btnPlay;
    private Toolbar toolbar;

    private CoordinatorLayout coordinatorLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView recyclerViewSonglist;
    private SonglistAdapter songlistAdapter;

    private User user;

    private ArrayList<Song> songList =  new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_detail);
        map();
        init();
        getUser();
        getLikedSongs(user.getId());

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
        collapsingToolbarLayout.setTitle("Bài hát yêu thích");

        // Hình mặc định cho danh sách bài hát yêu thích
        imageView.setImageResource(R.drawable.heart_img);
    }

    private void map() {
        coordinatorLayout = findViewById(R.id.coordinator_playlist_detail);
        collapsingToolbarLayout = findViewById(R.id.collapsingtoolbarlayout_playlist_detail);
        imageView = findViewById(R.id.imageview_playlist_detail);
        btnPlay = findViewById(R.id.button_play_playlist_detail);
        toolbar = findViewById(R.id.toolbar_playlist_detail);

        // Thiết lập recyclerView hiển thị hàng dọc
        recyclerViewSonglist = findViewById(R.id.recyclerview_songlist_playlist_detail);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(LikedSongListActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerViewSonglist.setLayoutManager(mLayoutManager);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_play_playlist_detail:
                Intent intent = new Intent(LikedSongListActivity.this, PlayActivity.class);
                intent.putExtra("SONGLIST", songList);
                LikedSongListActivity.this.startActivity(intent);
        }
    }

    private void getLikedSongs (int userId) {
        songList = new ArrayList<>();

        new LikeSongData().getLikeSongbyUserId(userId, new LikeSongAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Song> songs) {
                songList = songs;
                songlistAdapter = new SonglistAdapter(LikedSongListActivity.this, R.layout.songlist_item, songList);
                recyclerViewSonglist.setAdapter(songlistAdapter);
            }
        });
    }

    private void getUser() {
        user = new User();
        SharedPreferences sharedPref = getSharedPreferences("USER", Context.MODE_PRIVATE);
        user.setId(sharedPref.getInt("user_id", -1));
        user.setName(sharedPref.getString("user_name", ""));
        user.setEmail(sharedPref.getString("user_email", ""));
    }
}
