package com.myteam.myapplication.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myteam.myapplication.R;
import com.myteam.myapplication.adapter.AlbumAdapter;
import com.myteam.myapplication.data.AlbumData;
import com.myteam.myapplication.data.NewestAlbumsAsyncRespone;
import com.myteam.myapplication.model.Album;
import com.myteam.myapplication.ui.SpacesItemDecoration;

import java.util.ArrayList;

public class MoreAlbumActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    AlbumAdapter albumAdapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_album);

        mapping();
        loadData();
    }

    public void mapping() {
        recyclerView = findViewById(R.id.recyclerview_more_album);
        RecyclerView.LayoutManager mLayoutmanager = new GridLayoutManager(MoreAlbumActivity.this,2);
        recyclerView.setLayoutManager(mLayoutmanager);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

        toolbar = findViewById(R.id.toolbar_more_album);

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

    public void loadData () {
        new AlbumData().getNewestAlbums(new NewestAlbumsAsyncRespone() {
            @Override
            public void processFinished(ArrayList<Album> albums) {
                albumAdapter = new AlbumAdapter(MoreAlbumActivity.this, R.layout.album_square_item, albums);
                recyclerView.setAdapter(albumAdapter);
            }
        });
    }
}
