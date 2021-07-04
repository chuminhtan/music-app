package com.myteam.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myteam.myapplication.R;
import com.myteam.myapplication.adapter.AlbumAdapter;
import com.myteam.myapplication.data.ArtistAlbumsAsyncRespone;
import com.myteam.myapplication.data.ArtistAsyncRespone;
import com.myteam.myapplication.data.ArtistData;
import com.myteam.myapplication.model.Album;
import com.myteam.myapplication.model.AlbumSong;
import com.myteam.myapplication.model.Artist;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ArtistActivity extends AppCompatActivity {
    private ImageView imgArtist;
    private TextView txtArtistName;
    private TextView txtArtistStory;
    private RecyclerView recyclerViewAlbums;
    private AlbumAdapter  albumAdapter;

    private ArrayList<Album> mAlbums;
    private Artist artistIntent;
    private Artist mArtist;
    Toolbar toolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);

        init();
        getArtistIntent();

        // Check intent is not null
        if (artistIntent != null && !artistIntent.getName().equals("")) {
//            getInfoArtist(artistIntent.getId());
            setInfroArtist(artistIntent);
            Picasso.with(this).load(artistIntent.getUrlImg()).into(imgArtist);
            getAlbumList(artistIntent.getId());
        }
    }

    public void init() {
        toolbar = findViewById(R.id.toolbar_artist);
        txtArtistName = findViewById(R.id.textview_artist_name);
        txtArtistStory = findViewById(R.id.textview_artist_story);
        imgArtist = findViewById(R.id.imgview_artist);

        // Thiết lập recyclerView hiển thị hàng ngang
        recyclerViewAlbums = findViewById(R.id.recyclerview_artist_album_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ArtistActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewAlbums.setLayoutManager(layoutManager);

        // Create Toolbar
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

    public void getArtistIntent () {
        Intent intent = getIntent();

        if(intent != null) {
            // Data: Artist
            if (intent.hasExtra("artist")) {
                artistIntent = (Artist) intent.getSerializableExtra("artist");
                Toast.makeText(ArtistActivity.this, artistIntent.getName(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void getInfoArtist (int artistID) {
        mArtist = new Artist();
        new ArtistData().getArtistInfoById(new ArtistAsyncRespone() {
            @Override
            public void processFinished(Artist artist) {
                mArtist = artist;
                txtArtistName.setText(mArtist.getName());
                txtArtistStory.setText(mArtist.getStory());
            }
        }, artistID);

    }

    public void setInfroArtist (Artist artist) {
        txtArtistName.setText(artist.getName());
        txtArtistStory.setText(artist.getStory());

    }

    public void getAlbumList (int artistId) {
        mAlbums = new ArrayList<>();

        new ArtistData().getArtistAlbumbyId(artistId, new ArtistAlbumsAsyncRespone() {
            @Override
            public void processFinished(ArrayList<Album> albums) {
                mAlbums = albums;
                albumAdapter = new AlbumAdapter(ArtistActivity.this, R.layout.album_square_item, mAlbums);
                recyclerViewAlbums.setAdapter(albumAdapter);
            }
        });
    }

}
