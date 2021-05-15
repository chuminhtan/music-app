package com.myteam.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.myteam.myapplication.R;
import com.myteam.myapplication.model.Song;

public class PlayActivity extends AppCompatActivity {
    Toolbar toolbarPlay;
    ViewPager viewPagerPlay;
    ImageView imvLikeSong, imvShareSong, imvAddPlaylist, imvRepeat, imvSkipPre, imvPlay, imvSkipNext, imvShuffle;
    TextView txtTimePlayed, txtTimeTotal, txtTitleToolbarPlay;
    SeekBar seekBarPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);

        initComponent();

        Intent intent = getIntent();
        if (intent.hasExtra("song")) {
            Song song = (Song) intent.getSerializableExtra("song");
            Toast.makeText(this, song.getName(), Toast.LENGTH_SHORT).show();
        }
    }

    // Khởi tạo các thành phần từ xml
    private void initComponent() {
        // Toolbar
        toolbarPlay = findViewById(R.id.toolbar_play);
        // Viewpager
        viewPagerPlay = findViewById(R.id.viewpager_play);
        // ImageView
        imvLikeSong = findViewById(R.id.imageview_like_song);
        imvShareSong = findViewById(R.id.imageview_share_song);
        imvAddPlaylist = findViewById(R.id.imageview_list_add);
        imvShareSong = findViewById(R.id.imageview_share_song);
        imvShareSong = findViewById(R.id.imageview_share_song);
        imvRepeat = findViewById(R.id.imageview_repeat_song);
        imvSkipPre = findViewById(R.id.imageview_skip_previous_song);
        imvPlay = findViewById(R.id.imageview_play_song);
        imvSkipNext = findViewById(R.id.imageview_skip_next_song);
        imvShuffle =  findViewById(R.id.imageview_shuffle_song);
        // TextView
        txtTimePlayed = findViewById(R.id.textview_time_played);
        txtTimeTotal = findViewById(R.id.textview_time_total);
        txtTitleToolbarPlay = toolbarPlay.findViewById(R.id.textview_title_toolbar_play);
        //Seekbar
        seekBarPlay = findViewById(R.id.seekbar_time);
        // Tạo thanh toolbar
        setSupportActionBar(toolbarPlay);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbarPlay.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}