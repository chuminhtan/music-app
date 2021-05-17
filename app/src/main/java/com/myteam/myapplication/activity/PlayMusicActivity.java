package com.myteam.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.myteam.myapplication.R;
import com.myteam.myapplication.adapter.ViewPagerPlayAdapter;
import com.myteam.myapplication.fragment.CurrentPlaylistFragment;
import com.myteam.myapplication.fragment.DishFragment;
import com.myteam.myapplication.model.Playlist;
import com.myteam.myapplication.model.Song;
import com.myteam.myapplication.util.ServerInfo;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class PlayMusicActivity extends AppCompatActivity {

    public static ArrayList<Song> songList = new ArrayList<>();

    ImageButton btn_back;
    ImageButton btn_like_song;
    ImageButton btn_more_option;
    ImageButton btn_list_add;
    ImageButton btn_play;
    ImageButton btn_share;
    ImageButton btn_skip_next;
    ImageButton btn_skip_previous;
    ImageButton btn_shuffle;

    Toolbar toolbar;
    TextView txt_song_name;
    TextView txt_song_artist;
    TextView txt_time_played;
    TextView txt_time_song;
    SeekBar seekbar_time;
    ImageView image_song;
    MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    Song song;

//    ViewPager viewPager;
//    DishFragment dishFragment;
//    CurrentPlaylistFragment currentPlaylistFragment;
//  public static ViewPagerPlayAdapter adapterPlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_music);
//
//        mapping();
//
//        getDataIntent();
//
//        init();

    }

    private void getDataIntent() {
        songList.clear();

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("song")) {
                song = (Song) intent.getSerializableExtra("song");
                songList.add(song);
            }

            if (intent.hasExtra("songList")) {
                ArrayList<Song> songs = (ArrayList<Song>) intent.getSerializableExtra("songList");

            }
        }

    }

    private void init() {
        txt_song_name.setText(song.getName());
        txt_song_artist.setText(song.getArtistsName());

        toolbar.setTitle(song.getName());
        Picasso.with(PlayMusicActivity.this).load(song.getUrlImage()).into(image_song);

        seekbar_time.setMax(100);
        mediaPlayer = new MediaPlayer();

        prepareMediaPlayer(song.getUrlSrc());

        mediaPlayer.start();
        btn_play.setImageResource(R.drawable.ic_baseline_pause_circle_filled_50);
        updateSeekBar();

        seekbar_time.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                SeekBar seekBar = (SeekBar) v;
                int playPosition = (mediaPlayer.getDuration() / 100) * seekBar.getProgress();
                mediaPlayer.seekTo(playPosition);
                txt_time_played.setText(milliSecondsToTimer(mediaPlayer.getCurrentPosition()));
                return false;
            }
        });


        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    handler.removeCallbacks(update);
                    mediaPlayer.pause();
                    btn_play.setImageResource(R.drawable.ic_baseline_play_circle_90);
                } else {
                    mediaPlayer.start();
                    btn_play.setImageResource(R.drawable.ic_baseline_pause_circle_filled_50);
                    updateSeekBar();
                }
            }
        });
    }

    private void mapping() {
//
//        btn_like_song = findViewById(R.id.btn_like_song);
//        btn_more_option = findViewById(R.id.btn_more_option);
//        btn_list_add = findViewById(R.id.btn_list_add);
//        btn_play = findViewById(R.id.btn_play);
//        btn_share = findViewById(R.id.btn_share);
//        btn_list = findViewById(R.id.btn_list);
//        btn_skip_next = findViewById(R.id.btn_skip_next);
//        btn_volume = findViewById(R.id.btn_volume);
//        btn_skip_previous = findViewById(R.id.btn_skip_previous);
//        btn_mode_play = findViewById(R.id.btn_mode_play);
//        txt_song_name = findViewById(R.id.txt_song_name);
//        txt_song_artist = findViewById(R.id.txt_song_artist);
//        txt_time_played = findViewById(R.id.txt_time_played);
//        txt_time_song = findViewById(R.id.txt_time_song);
//        seekbar_time = findViewById(R.id.seekbar_time);
//        image_song = findViewById(R.id.image_song);
//
//        toolbar = findViewById(R.id.toolbar_playsong);
//
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        viewPager = findViewById(R.id.viewpager_play);
//        adapterPlay = new ViewPagerPlayAdapter(getSupportFragmentManager());
//        dishFragment = new DishFragment();
//        currentPlaylistFragment = new CurrentPlaylistFragment();
//        adapterPlay.AddFragment(currentPlaylistFragment);
//        adapterPlay.AddFragment(dishFragment);
//        viewPager.setAdapter(adapterPlay);
    }

    private void prepareMediaPlayer(String url) {
        Log.d("URL", url);
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            txt_time_song.setText(milliSecondsToTimer(mediaPlayer.getDuration()));
        } catch (Exception ex) {
            Toast.makeText(PlayMusicActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private Runnable update = new Runnable() {
        @Override
        public void run() {
            updateSeekBar();
            long currentDuration = mediaPlayer.getCurrentPosition();
            txt_time_played.setText(milliSecondsToTimer(currentDuration));
        }
    };

    private void updateSeekBar() {
        if (mediaPlayer.isPlaying()) {
            seekbar_time.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration()) * 100));
            handler.postDelayed(update, 1000);
        }
    }

    private String milliSecondsToTimer(long milliSeconds) {
        String timeString = "";
        String secondString;

        int hours = (int) (milliSeconds / (1000 * 60 * 60));
        int minutes = (int) (milliSeconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliSeconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        if (hours > 0) {
            timeString = hours + ":";
        }
        if (seconds < 10) {
            secondString = "0" + seconds;
        } else {
            secondString = "" + seconds;
        }

        timeString = timeString + minutes + ":" + secondString;
        return timeString;
    }


}
