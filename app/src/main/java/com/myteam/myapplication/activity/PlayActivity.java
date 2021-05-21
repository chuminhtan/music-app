package com.myteam.myapplication.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.palette.graphics.Palette;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.myteam.myapplication.R;
import com.myteam.myapplication.adapter.ViewPagerPlayAdapter;

import com.myteam.myapplication.fragment.CurrentPlaylistFragment;
import com.myteam.myapplication.fragment.DishFragment;
import com.myteam.myapplication.model.Song;
import com.myteam.myapplication.service.MusicService;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Collections;


import static com.myteam.myapplication.controller.AppController.ACTION_NEXT;
import static com.myteam.myapplication.controller.AppController.ACTION_PLAY;
import static com.myteam.myapplication.controller.AppController.ACTION_PREVIOUS;
import static com.myteam.myapplication.controller.AppController.CHANNEL_ID_2;

public class PlayActivity extends AppCompatActivity implements ActionPlaying, ServiceConnection {

    Toolbar toolbarPlay;
    ViewPager viewPagerPlay;
    ImageView btnLikeSong, btnShareSong, btnAddPlaylist, btnRepeatOne, btnSkipPre, btnPlayPause, btnSkipNext, btnShuffle;
    TextView txtSongName, txtSongArtist, txtTimePlayed, txtTimeTotal, txtTitleToolbarPlay;
    SeekBar seekBarPlay;
    ViewPager viewPager;
    DishFragment dishFragment;
    CurrentPlaylistFragment currentPlaylistFragment;
    //    MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    public static ArrayList<Song> SONGLIST = new ArrayList<>();
    public static ArrayList<Song> SONGLIST_SHUFFLE = new ArrayList<>();
    public static ViewPagerPlayAdapter adapterPlay;
    public static boolean isShuffle = false, isRepeatOne = false;
    private Thread playThread, prevThread, nextThread;
    private Bundle bundle = new Bundle();
    private int currentPositionSong = 0;
    private String urlMP3 = "";
    private int sizeList;
    boolean isActivityVisible;

    MusicService musicService;

    // ON CREATE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);
        // Get data intent
        getDataIntent();
        isActivityVisible = true;
    }


    // INIT COMPONENT
    private void initComponent() {

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
        btnRepeatOne = findViewById(R.id.imageview_repeat_song);
        btnSkipPre = findViewById(R.id.imageview_skip_previous_song);
        btnPlayPause = findViewById(R.id.imageview_play_song);
        btnSkipNext = findViewById(R.id.imageview_skip_next_song);
        btnShuffle = findViewById(R.id.imageview_shuffle_song);
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
    }


    private void prevThreadBtn() {
        prevThread = new Thread() {
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
        nextThread = new Thread() {
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
        playThread = new Thread() {
            @Override
            public void run() {
                super.run();
                btnPlayPause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnPlayPauseClicked();
                    }
                });
            }
        };
        playThread.start();
    }

    public void btnSkipPreClicked() {
        if (SONGLIST.size() > 1) {
            currentPositionSong = (currentPositionSong - 1) < 0 ? (SONGLIST.size() - 1) : ((currentPositionSong - 1) % SONGLIST.size());
            playSongs();
        }
    }

    public void btnSkipNextClicked() {
        if (isShuffle && !isRepeatOne) {
            int temp = currentPositionSong;
            do {
                currentPositionSong = (int) Math.floor(Math.random() * (sizeList - 1 + 1) + 0);
            } while (currentPositionSong == temp);

        } else if (SONGLIST.size() > 1 && !isRepeatOne) {
            currentPositionSong = (currentPositionSong + 1) % SONGLIST.size();
        }

        playSongs();
    }

    public void btnPlayPauseClicked() {
        Intent intent = new Intent(PlayActivity.this, MusicService.class);


        if (musicService.isPlaying()) {
            btnPlayPause.setImageResource(R.drawable.ic_play_circle);
            handler.removeCallbacks(update);
            musicService.pause();
            dishFragment.pauseAnimator();
            intent.putExtra("isPause", false);
        } else {
            btnPlayPause.setImageResource(R.drawable.ic_pause_circle);
//            musicService.showNotification(R.drawable.ic_pause_circle);
            musicService.start();
            updateSeekBar();
            dishFragment.resumeAnimator();
            intent.putExtra("isPause", true);
        }
        startService(intent);
    }

    public void btnShuffleClicked() {
        if (SONGLIST_SHUFFLE.size() == 0) return;

        if (!isShuffle) {
            btnShuffle.setImageResource(R.drawable.ic_shuffle_on);
        } else {
            btnShuffle.setImageResource(R.drawable.ic_shuffle_off);
        }

        isShuffle = !isShuffle;
    }

    @Override
    public void playsong(int position) {
        currentPositionSong = position;
        playSongs();
    }

    public void btnRepeatOneClicked() {
        if (!isRepeatOne) {
            btnRepeatOne.setImageResource(R.drawable.ic_repeatone_on);
        } else {
            btnRepeatOne.setImageResource(R.drawable.ic_repeat_off);
        }
        isRepeatOne = !isRepeatOne;
    }


    // GET DATA FROM INTENT
    private void getDataIntent() {
        SONGLIST.clear();
        SONGLIST_SHUFFLE.clear();

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("song")) {
                Song song = (Song) intent.getSerializableExtra("song");
                SONGLIST.add(song);
            }

            if (intent.hasExtra("SONGLIST")) {
                ArrayList<Song> songs = (ArrayList<Song>) intent.getSerializableExtra("SONGLIST");
                SONGLIST = new ArrayList<>(songs);
                SONGLIST_SHUFFLE = new ArrayList<>(songs);
                Collections.shuffle(SONGLIST_SHUFFLE);

                sizeList = SONGLIST.size();
            }

        }
    }


    // PLAYS SONGS
    private void playSongs() {

        if (SONGLIST.size() == 0) return;

        Song song = SONGLIST.get(currentPositionSong);

        if (musicService.isPlaying()) {
            musicService.stop();
            musicService.reset();
            musicService.release();
        }

        // Send position to Service
        Intent intent = new Intent(PlayActivity.this, MusicService.class);
        intent.putExtra("servicePosition", currentPositionSong);
        intent.putExtra("isPause", true);
        startService(intent);

        // Change background
        changeBackground(song.getUrlImage());

        musicService.createMediaPlayer();

        seekBarPlay.setMax(100);
        prepareMediaPlayer(song.getUrlSrc());
        musicService.start();

        // Change song name image
        if (isActivityVisible) {
            // change icon play/pause
            btnPlayPause.setImageResource(R.drawable.ic_pause_circle);
            updateSeekBar();
            changeSongImageFromDishFragment(song.getUrlImage());
            // Set song name
            txtSongName.setText(song.getName());
            txtSongArtist.setText(song.getArtistsName());
        }


        // Change Song When Complete
        musicService.onCompleted(isShuffle);

        // Change Notification
//        musicService.showNotification(R.drawable.ic_play_circle);
    }


    // CHANGE SONG IMAGE FROM DISH FRAGMENT
    private void changeSongImageFromDishFragment(final String urlImage) {

        if (dishFragment.isExistsViewComponent()) {
            dishFragment.changeCircleImage(urlImage);
        } else {
            bundle.putString("urlImage", urlImage);
            dishFragment.setArguments(bundle);
        }

    }


    // CHANGE BACKGROUND Activity
    private void changeBackground(final String urlImage) {
        Animation animationOut = AnimationUtils.loadAnimation(PlayActivity.this, android.R.anim.fade_out);
        Animation animationIn = AnimationUtils.loadAnimation(PlayActivity.this, android.R.anim.fade_in);
        Target target = new com.squareup.picasso.Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(@Nullable Palette palette) {
//                        Palette.Swatch swatch = palette.getLightMutedSwatch();
//                        Palette.Swatch swatch = palette.getLightMutedSwatch();
                        Palette.Swatch swatch = palette.getMutedSwatch();

                        if (swatch != null) {
                            LinearLayout linearLayout = findViewById(R.id.linearlayout_media_player);

                            linearLayout.setBackgroundResource(R.drawable.main_background);

                            GradientDrawable gradientDrawableBg = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                    new int[]{

                                            swatch.getRgb(),
                                            Color.rgb(0, 0, 0)});

                            //Color.rgb(193,193,193)
                            linearLayout.setBackground(gradientDrawableBg);
//                            txtSongName.setTextColor(swatch.getBodyTextColor());
//                            txtSongArtist.setTextColor(swatch.getBodyTextColor());

                        } else {
                            LinearLayout linearLayout = findViewById(R.id.linearlayout_media_player);

                            linearLayout.setBackgroundResource(R.drawable.main_background);
                            GradientDrawable gradientDrawableBg = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                    new int[]{0xff000000, 0xff000000});

                            linearLayout.setBackground(gradientDrawableBg);

//                            txtSongName.setTextColor(Color.WHITE);
//                            txtSongArtist.setTextColor(Color.DKGRAY);
                        }
                    }
                });
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.d("ON_BITMAP_FAILED", errorDrawable.toString());
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(PlayActivity.this).load(urlImage).into(target);
    }


    // SET EVENTS
    @SuppressLint("ClickableViewAccessibility")
    private void setEvents() {
        // Seekbar time
        seekBarPlay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                SeekBar seekBar = (SeekBar) v;
                int playPosition = (musicService.getDuration() / 100) * seekBar.getProgress();
                musicService.seekTo(playPosition);
                txtTimePlayed.setText(milliSecondsToTimer(musicService.getCurrentPosition()));
                return false;
            }
        });

        // Shuffle Button
        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnShuffleClicked();
            }
        });

        // Repeat One Button
        btnRepeatOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRepeatOneClicked();
            }
        });

        // Next
        nextThreadBtn();
        prevThreadBtn();
//        shuffleThreadBtn();
        playThreadBtn();
//        repeatOneThreadBtn();

    }

    // CHANGE PLAY/PAUSE BUTTON
    private void changePlayPause() {
        if (musicService.isPlaying()) {
            btnPlayPause.setImageResource(R.drawable.ic_play_circle);
            handler.removeCallbacks(update);
            musicService.pause();
            dishFragment.pauseAnimator();
        } else {
            btnPlayPause.setImageResource(R.drawable.ic_pause_circle);
            musicService.start();
            updateSeekBar();
            dishFragment.resumeAnimator();
        }

    }


    // PREPARE MEDIA PLAYER
    private void prepareMediaPlayer(String url) {
        Log.d("URL", url);
        try {
            musicService.setDataSource(url);
            musicService.prepare();
            txtTimeTotal.setText(milliSecondsToTimer(musicService.getDuration()));
        } catch (Exception ex) {
            Toast.makeText(PlayActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    // UPDATE SEEK BAR
    private Runnable update = new Runnable() {
        @Override
        public void run() {
            updateSeekBar();
            long currentDuration = musicService.getCurrentPosition();
            txtTimePlayed.setText(milliSecondsToTimer(currentDuration));
        }
    };

    private void updateSeekBar() {
        if (musicService.isPlaying()) {
            seekBarPlay.setProgress((int) (((float) musicService.getCurrentPosition() / musicService.getDuration()) * 100));
            handler.postDelayed(update, 1000);
        }
    }

    private String milliSecondsToTimer(long milliSeconds) {
        String timeString = "";
//        String secondString;
//
//        int hours = (int) (milliSeconds / (1000 * 60 * 60));
//        int minutes = (int) (milliSeconds % (1000 * 60 * 60)) / (1000 * 60);
//        int seconds = (int) ((milliSeconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
//
//        if (hours > 0) {
//            timeString = hours + ":";
//        }
//        if (seconds < 10) {
//            secondString = "0" + seconds;
//        } else {
//            secondString = "" + seconds;
//        }

        long m = (milliSeconds / 1000) / 60;

        // formula for conversion for
        // milliseconds to seconds
        long s = (milliSeconds / 1000) % 60;

//        timeString = timeString + minutes + ":" + secondString;
        timeString = timeString+m+":"+s;
        return timeString;
    }


    // IMAGE ANIMATION
    public void imageAnimation(final Context context, ImageView imageView, final Bitmap bitmap) {
        Animation animationOut = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        Animation animationIn = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);

        animationOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }


    /**
     * **************************************************************************************
     * CONNECT TO SERVICE
     * **************************************************************************************
     */

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MusicService.MyBinder myBinder = (MusicService.MyBinder) service;
        musicService = myBinder.getService();
        musicService.setCallback(this);
//        Toast.makeText(PlayActivity.this, "Connected " + musicService,Toast.LENGTH_LONG).show();
        initComponent();
        playSongs();
        setEvents();
//        PlayActivity.this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                initComponent();
//                playSongs();
//                setEvents();
//            }
//        });

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        musicService = null;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (SONGLIST != null) {
            Intent intent = new Intent(PlayActivity.this, MusicService.class);
            intent.putExtra("songlist", SONGLIST);
            startService(intent);
        } else {
            getDataIntent();
            playSongs();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

    }


    // ON RESUME
    // HANDEL MEDIA CONTROL
    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(PlayActivity.this, MusicService.class);
        bindService(intent, PlayActivity.this, BIND_AUTO_CREATE);
        isActivityVisible = true;

    }

    // ON PAUSE
    @Override
    protected void onPause() {
        super.onPause();
        isActivityVisible = false;
        unbindService(PlayActivity.this);
    }
}