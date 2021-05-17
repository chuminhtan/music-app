package com.myteam.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.myteam.myapplication.R;
import com.myteam.myapplication.adapter.ViewPagerPlayAdapter;
import com.myteam.myapplication.fragment.CurrentPlaylistFragment;
import com.myteam.myapplication.fragment.DishFragment;
import com.myteam.myapplication.model.Song;

import java.util.ArrayList;

public class PlayActivity extends AppCompatActivity {
    Toolbar toolbarPlay;
    ViewPager viewPagerPlay;
    ImageView imvLikeSong, imvShareSong, imvAddPlaylist, imvRepeat, imvSkipPre, imvPlay, imvSkipNext, imvShuffle;
    TextView txtTimePlayed, txtTimeTotal, txtTitleToolbarPlay;
    SeekBar seekBarPlay;
    ViewPager viewPager;
    DishFragment dishFragment;
    CurrentPlaylistFragment currentPlaylistFragment;
    MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    public static ArrayList<Song> SONGLIST = new ArrayList<>();
    
    public static ViewPagerPlayAdapter adapterPlay;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);
        getDataIntent();

        initComponent();
        
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
        txtTitleToolbarPlay = findViewById(R.id.textview_song_name_toolbar_play);

        //Seekbar
        seekBarPlay = findViewById(R.id.seekbar_play);
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
        viewPager = findViewById(R.id.viewpager_play);
        adapterPlay = new ViewPagerPlayAdapter(getSupportFragmentManager());
        dishFragment = new DishFragment();
        currentPlaylistFragment = new CurrentPlaylistFragment();
        adapterPlay.AddFragment(dishFragment);
        adapterPlay.AddFragment(currentPlaylistFragment);
        viewPager.setAdapter(adapterPlay);

        mediaPlayer = new MediaPlayer();
        // play songs
        playSong();

        // set events
        setEvents();

    }

    private void getDataIntent() {
        SONGLIST.clear();

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("song")) {
                Song song = (Song) intent.getSerializableExtra("song");
                SONGLIST.add(song);
            }

            if (intent.hasExtra("SONGLIST")) {
                ArrayList<Song> songs = (ArrayList<Song>) intent.getSerializableExtra("SONGLIST");
                SONGLIST = songs;
            }
        }

    }


    private void playSong() {
        if (SONGLIST.size() == 0) return;
            Song song = SONGLIST.get(0);

            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }

            seekBarPlay.setMax(100);
            prepareMediaPlayer(song.getUrlSrc());
            mediaPlayer.start();

            // change icon play/pause
            imvPlay.setImageResource(R.drawable.ic_pause_circle);
            // update seekbar
            updateSeekBar();

            // set image
        Bundle bundle = new Bundle();
        bundle.putString("songImageURL", song.getUrlImage());
        dishFragment.setArguments(bundle);

    }

    private void setEvents() {
        seekBarPlay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                SeekBar seekBar = (SeekBar) v;
                int playPosition = (mediaPlayer.getDuration() / 100) * seekBar.getProgress();
                mediaPlayer.seekTo(playPosition);
                txtTimePlayed.setText(milliSecondsToTimer(mediaPlayer.getCurrentPosition()));
                return false;
            }
        });


        imvPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePlayPause();
            }
        });
    }

    private void changePlayPause() {
        if ( mediaPlayer.isPlaying()) {
            imvPlay.setImageResource(R.drawable.ic_play_circle);
            handler.removeCallbacks(update);
            mediaPlayer.pause();
            dishFragment.pauseAnimator();
        } else {
            imvPlay.setImageResource(R.drawable.ic_pause_circle);
            mediaPlayer.start();
            updateSeekBar();
            dishFragment.resumeAnimator();
        }

    }

    private void prepareMediaPlayer(String url) {
        Log.d("URL", url);
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            txtTimeTotal.setText(milliSecondsToTimer(mediaPlayer.getDuration()));
        } catch (Exception ex) {
            Toast.makeText(PlayActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private Runnable update = new Runnable() {
        @Override
        public void run() {
            updateSeekBar();
            long currentDuration = mediaPlayer.getCurrentPosition();
            txtTimePlayed.setText(milliSecondsToTimer(currentDuration));
        }
    };

    private void updateSeekBar() {
        if (mediaPlayer.isPlaying()) {
            seekBarPlay.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration()) * 100));
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