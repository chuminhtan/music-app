package com.myteam.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class PlayActivity extends AppCompatActivity {
    Toolbar toolbarPlay;
    ViewPager viewPagerPlay;
    ImageView btnLikeSong, btnShareSong, btnAddPlaylist, btnRepeat, btnSkipPre, btnPlayPause, btnSkipNext, btnShuffle;
    TextView txtSongName, txtSongArtist,txtTimePlayed, txtTimeTotal, txtTitleToolbarPlay;
    SeekBar seekBarPlay;
    ViewPager viewPager;
    DishFragment dishFragment;
    CurrentPlaylistFragment currentPlaylistFragment;
    MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    public static ArrayList<Song> SONGLIST = new ArrayList<>();
    public static ViewPagerPlayAdapter adapterPlay;
    private Thread playThread, prevThread, nextThread;
private         Bundle bundle = new Bundle();
    private int currentPositionSong = 0;
    private String urlMP3 = "";
    // ON CREATE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        initComponent();
        
    }


    // ON RESUME
    // HANDEL MEDIA CONTROL
    @Override
    protected void onResume() {
        playThreadBtn();
        nextThreadBtn();
        prevThreadBtn();
        super.onResume();
    }

    private void prevThreadBtn() {
        prevThread = new Thread(){
            @Override
            public void run() {
                super.run();
                btnSkipPre.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnSkipPreClicked();
                    }
                });
            }
        };
        prevThread.start();
    }

    private void nextThreadBtn() {
        nextThread = new Thread(){
            @Override
            public void run() {
                super.run();
                btnSkipNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnSkipNextClicked();
                    }
                });
            }
        };
        nextThread.start();
    }

    private void playThreadBtn() {
        playThread = new Thread(){
            @Override
            public void run() {
                super.run();
                btnPlayPause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playPauseBtnClicked();
                    }
                });
            }
        };
        playThread.start();
    }

    private void btnSkipPreClicked() {
        if (SONGLIST.size() > 1) {
            currentPositionSong = (currentPositionSong - 1) < 0 ? (SONGLIST.size() - 1) : ((currentPositionSong - 1) % SONGLIST.size());
            playSong();
        }
    }

    private void btnSkipNextClicked() {
        if (SONGLIST.size() > 1) {
            currentPositionSong = (currentPositionSong + 1) % SONGLIST.size();
            playSong();
        }
    }



    private void playPauseBtnClicked() {
        if ( mediaPlayer.isPlaying()) {
            btnPlayPause.setImageResource(R.drawable.ic_play_circle);
            handler.removeCallbacks(update);
            mediaPlayer.pause();
            dishFragment.pauseAnimator();
        } else {
            btnPlayPause.setImageResource(R.drawable.ic_pause_circle);
            mediaPlayer.start();
            updateSeekBar();
            dishFragment.resumeAnimator();
        }
    }


    // INIT VIEW COMPONENT
    private void initComponent() {
        // Get data intent
        getDataIntent();

        // Toolbar
        toolbarPlay = findViewById(R.id.toolbar_play);
        // Viewpager
        viewPagerPlay = findViewById(R.id.viewpager_play);
        // ImageView
        btnLikeSong = findViewById(R.id.imageview_like_song);
        btnShareSong = findViewById(R.id.imageview_share_song);
        btnAddPlaylist = findViewById(R.id.imageview_list_add);
        btnShareSong = findViewById(R.id.imageview_share_song);
        btnShareSong = findViewById(R.id.imageview_share_song);
        btnRepeat = findViewById(R.id.imageview_repeat_song);
        btnSkipPre = findViewById(R.id.imageview_skip_previous_song);
        btnPlayPause = findViewById(R.id.imageview_play_song);
        btnSkipNext = findViewById(R.id.imageview_skip_next_song);
        btnShuffle =  findViewById(R.id.imageview_shuffle_song);
        // TextView
        txtTimePlayed = findViewById(R.id.textview_time_played);
        txtTimeTotal = findViewById(R.id.textview_time_total);
        txtTitleToolbarPlay = findViewById(R.id.textview_song_name_toolbar_play);
        txtSongName = findViewById((R.id.textview_song_name_toolbar_play));
        txtSongArtist = findViewById(R.id.textview_song_artist_toolbar_play);
        //Seekbar
        seekBarPlay = findViewById(R.id.seekbar_play);
        // Tạo thanh toolbar
        setSupportActionBar(toolbarPlay);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbarPlay.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
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


    // GET DATA FROM INTENT
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


    // PLAYS SONGS
    private void playSong() {
        if (SONGLIST.size() == 0) return;
            Song song = SONGLIST.get(currentPositionSong);

            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }

            mediaPlayer = new MediaPlayer();
            seekBarPlay.setMax(100);
            prepareMediaPlayer(song.getUrlSrc());
            mediaPlayer.start();

            // change icon play/pause
            btnPlayPause.setImageResource(R.drawable.ic_pause_circle);
            // update seekbar
            updateSeekBar();

            // set image
            changeSongImageFromDishFragment(song.getUrlImage());
        // Set song name
        txtSongName.setText(song.getName());
        txtSongArtist.setText(song.getArtistsName());

    }


    // CHANGE SONG IMAGE FROM DISH FRAGMENT
    private void changeSongImageFromDishFragment (String urlImage) {
        if (dishFragment.isExistsViewComponent()) {
            dishFragment.changeCircleImage(urlImage);
        } else {
            bundle.putString("urlImage", urlImage);
            dishFragment.setArguments(bundle);
        }

    }


    // CHANGE BACKGROUND Activity
    private void changeBackground(String urlImage) {

        Target target = new com.squareup.picasso.Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(PlayActivity.this).load(urlImage).into(target);
    }


    // SET EVENTS
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


//        btnPlayPause.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                changePlayPause();
//            }
//        });
    }


    // CHANGE PLAY/PAUSE BUTTON
    private void changePlayPause() {
        if ( mediaPlayer.isPlaying()) {
            btnPlayPause.setImageResource(R.drawable.ic_play_circle);
            handler.removeCallbacks(update);
            mediaPlayer.pause();
            dishFragment.pauseAnimator();
        } else {
            btnPlayPause.setImageResource(R.drawable.ic_pause_circle);
            mediaPlayer.start();
            updateSeekBar();
            dishFragment.resumeAnimator();
        }

    }


    // PREPARE MEDIA PLAYER
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


    // UPDATE SEEK BAR
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