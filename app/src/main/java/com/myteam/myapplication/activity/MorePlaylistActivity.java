package com.myteam.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.myteam.myapplication.R;
import com.myteam.myapplication.adapter.PlaylistSquareAdapter;
import com.myteam.myapplication.data.PlaylistArrayListAsyncResponse;
import com.myteam.myapplication.data.PlaylistData;
import com.myteam.myapplication.model.Playlist;
import com.myteam.myapplication.ui.SpacesItemDecoration;

import java.util.ArrayList;

public class MorePlaylistActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    PlaylistSquareAdapter playlistSquareAdapter;
    TextView tvToolbar;
    Toolbar toolbar;
    int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_playlist);
        tvToolbar = findViewById(R.id.textview_toolbar_more_playlist);

        type = getTypeFromIntent();

        if (type == 1) {
            tvToolbar.setText("Chủ Đề Hot");
        } else if (type == 2) {
            tvToolbar.setText("Top 100");
        } else if (type == 3) {
            tvToolbar.setText("Tâm Trạng Và Hoạt Động");
        }
        mapping();
        loadData(type);
    }

    private void mapping() {
        recyclerView = findViewById(R.id.recyclerview_more_playlist);
        RecyclerView.LayoutManager mLayoutmanager = new GridLayoutManager(MorePlaylistActivity.this,2);
        recyclerView.setLayoutManager(mLayoutmanager);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

        toolbar = findViewById(R.id.toolbar_more_playlist);
        // Tạo thanh toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadData(int type) {
        new PlaylistData().getPlaylistsType(type,0, new PlaylistArrayListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Playlist> playlistArrayList) {
                playlistSquareAdapter = new PlaylistSquareAdapter(MorePlaylistActivity.this, R.layout.playlist_square_item ,playlistArrayList);
                recyclerView.setAdapter(playlistSquareAdapter);
            }
        });
    }

    private int getTypeFromIntent(){
        Intent intent = getIntent();
        return (int)intent.getSerializableExtra("type");
    }
}