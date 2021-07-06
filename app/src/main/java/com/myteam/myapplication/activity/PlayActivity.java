package com.myteam.myapplication.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
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
import com.myteam.myapplication.adapter.CreateUserPlaylistDialog;
import com.myteam.myapplication.adapter.PlaylistBottomSheetAdapter;
import com.myteam.myapplication.adapter.ViewPagerPlayAdapter;
import com.myteam.myapplication.data.CheckLikeSongAsyncResponse;
import com.myteam.myapplication.data.LikeSongAsyncRespone;
import com.myteam.myapplication.data.LikeSongData;
import com.myteam.myapplication.data.UserPlaylistAsycnResponse;
import com.myteam.myapplication.data.UserPlaylistData;
import com.myteam.myapplication.fragment.CurrentPlaylistFragment;
import com.myteam.myapplication.fragment.DishFragment;
import com.myteam.myapplication.model.LikeSong;
import com.myteam.myapplication.model.Playlist;
import com.myteam.myapplication.model.Song;
import com.myteam.myapplication.model.User;
import com.myteam.myapplication.service.MusicService;
import com.myteam.myapplication.util.ServerInfo;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class PlayActivity extends AppCompatActivity implements ActionPlaying, ServiceConnection, CreateUserPlaylistDialog.CreateUserPlaylistDialogListener {
    Toolbar toolbarPlay;
    ViewPager viewPagerPlay;
    ImageView btnLikeSong, btnAddPlaylist, btnRepeatOne, btnSkipPre, btnPlayPause, btnSkipNext, btnShuffle;
    TextView txtSongName, txtSongArtist, txtTimePlayed, txtTimeTotal, txtTitleToolbarPlay;
    SeekBar seekBarPlay;
    ViewPager viewPager;
    DishFragment dishFragment;
    CurrentPlaylistFragment currentPlaylistFragment;
    Handler handler = new Handler();
    private Thread playThread, prevThread, nextThread;
    public static ArrayList<Song> SONGLIST = new ArrayList<>();
    public static ArrayList<Song> SONGLIST_SHUFFLE = new ArrayList<>();
    public static ViewPagerPlayAdapter adapterPlay;
    public static boolean isShuffle = false, isRepeatOne = false;
    private Bundle bundle = new Bundle();
    private int currentPositionSong = 0;
    private int sizeList;
    boolean isActivityVisible;
    MusicService musicService;
    private User user;
    private String result, message;
    PlaylistBottomSheetAdapter bottomSheetAdapter;

    // ON CREATE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);
        // Get data intent
        getDataIntent();
        // Get data from Sharedpreferences
        getPreferences();
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

        btnAddPlaylist = findViewById(R.id.imageview_list_add);
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
            getDataIntent();
            currentPlaylistFragment.updateCurrentPlaylistView(SONGLIST_SHUFFLE);
            btnShuffle.setImageResource(R.drawable.ic_shuffle_on);
        } else {
            btnShuffle.setImageResource(R.drawable.ic_baseline_shuffle_50);
        }
        isShuffle = !isShuffle;
    }

    public void btnButtonLikeSong() {
        playThread = new Thread() {
            @Override
            public void run() {
                super.run();
                btnLikeSong.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LikeSong likeSong = new LikeSong();
                        likeSong.setUserId(user.getId());
                        Song song = SONGLIST.get(currentPositionSong);
                        likeSong.setSongId(song.getId());

                        like(likeSong);
                    }
                });
            }
        };
        playThread.start();
    }


    // event add playlist
    public void btnAddPlaylist() {
        playThread = new Thread() {
            @Override
            public void run() {
                super.run();
                btnAddPlaylist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("song_id", SONGLIST.get(currentPositionSong).getId());
                        bottomSheetAdapter = new PlaylistBottomSheetAdapter();
                        bottomSheetAdapter.setArguments(bundle);
                        bottomSheetAdapter.show(getSupportFragmentManager(), "PlaylistBottomSheet");
                    }
                });
            }
        };
        playThread.start();
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

    // Download Song
//    public void DownloadSong(String url, String songname) {
//        try {
//            DownloadManager downloadManager = (DownloadManager) this.getSystemService(DOWNLOAD_SERVICE);
//            Log.d("PLAYMUSIC", "from activity play, url of song " + url);
//            Uri uri = Uri.parse("http://192.168.1.6:8000/storage/song/Iz9P2BXyINKQhf69rDQ431tPYaBhwBgG5x1PvV3z.mp3");
//            DownloadManager.Request request = new DownloadManager.Request(uri);
//            request.allowScanningByMediaScanner();
//            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, songname);
//
//            Long reference = downloadManager.enqueue(request);
//        } catch (IllegalArgumentException e) {
//            Log.d("ERRROR DONWLOAD", e.getMessage());
//        }
//    }

    // GET DATA FROM PREFERENCES
    private void getPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", 0);
        String userName = sharedPreferences.getString("user_name", "");
        String userEmail = sharedPreferences.getString("user_email", "");
        Log.d("User Information: ", String.valueOf(userId) + " " + userName + " " + userEmail);
        user = new User(userId, userName, userEmail, "");
    }

    // CHECK AND REQUEST PERMISION
    private void requestPermisionWriteExternalStorage() {
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyHavePermission()) {
                requestForSpecificPermission();
            }
        }
    }

    // CHECK PERMISSION
    private Boolean checkIfAlreadyHavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //granted
                } else {
                    //not granted
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    // LIKE AND UNLIKE
    public void like(LikeSong likeSong) {
        new LikeSongData().like(likeSong, new LikeSongAsyncRespone() {
            @Override
            public void processFinished(Map<String, String> mapResponse) {
                result = mapResponse.get("result");
                message = mapResponse.get("message");

                Log.d("LIKESONG", "From PlayActivity-LikeSong Started");
                Log.d("LIKESONG", "From PlayActivity-LikeSong response : " + mapResponse.get("result") + " | " + mapResponse.get("message"));

                if (result.equalsIgnoreCase("success") && message.equalsIgnoreCase("like")) {
                    btnLikeSong.setImageResource(R.drawable.ic_baseline_favorite_50);
                }
                if (result.equalsIgnoreCase("success") && message.equalsIgnoreCase("unlike")) {
                    btnLikeSong.setImageResource(R.drawable.ic_favourite);
                }
            }
        });
    }

    // ADD New User playlist
    public void addNewUserPlaylist(Playlist playlist, Song song) {
        new UserPlaylistData().createUserPlaylist(playlist, song, new UserPlaylistAsycnResponse() {
            @Override
            public void processFinished(Map<String, String> mapResponse) {
                result = mapResponse.get("result");
                message = mapResponse.get("message");

                if (result.equalsIgnoreCase("success")) {
                    Log.d("USERPLAYLIST", "Add New User Playlist Successfully");
                    Bundle bundle = new Bundle();
                    bundle.putInt("song_id", SONGLIST.get(currentPositionSong).getId());
                    ;
                    bottomSheetAdapter.dismiss();
                    PlaylistBottomSheetAdapter new_bottomSheetAdapter = new PlaylistBottomSheetAdapter();
                    new_bottomSheetAdapter.setArguments(bundle);
                    new_bottomSheetAdapter.show(getSupportFragmentManager(), "PlaylistBottomSheet");
                }
            }
        });
    }

    // GET NEW PLAYLIST USER CREATE FROM DIALOG
    @Override
    public void applyText(String playListName) {
        Playlist playlist = new Playlist();
        Song song = SONGLIST.get(currentPositionSong);
        playlist.setType(0);
        playlist.setName(playListName);
        playlist.setUser(user);
        playlist.setImg(song.getImg());
        addNewUserPlaylist(playlist, song);
        Log.d("CREATE USER PLAYLIST", playListName);

    }

    // CHECK LIKE SONG
    public void checkLikeSong(int userId, int songId) {
        new LikeSongData().checkIfLikeSong(songId, userId, new CheckLikeSongAsyncResponse() {
            @Override
            public void processFinished(boolean result) {
                if (result == true) {
                    btnLikeSong.setImageResource(R.drawable.ic_baseline_favorite_50);
                } else {
                    btnLikeSong.setImageResource(R.drawable.ic_favourite);
                }
            }
        });
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

        seekBarPlay.setMax(100);
        if (song.getUri() == null) {
            // URL
            Log.d("PLAYMUSIC", "From PlayActivity, Uri ");
            musicService.createMediaPlayer(null, null);
            prepareMediaPlayer(song.getUrlSrc());
        } else {
            //URI
            Log.d("PLAYMUSIC", "From PlayActivity, Uri " + Uri.parse(song.getUri()));
            musicService.createMediaPlayer(getApplication(), Uri.parse(song.getUri()));
        }
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
        if (user.getId() !=0) {
            checkLikeSong(user.getId(), SONGLIST.get(currentPositionSong).getId());
        }
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
                            linearLayout.setBackground(gradientDrawableBg);
                        } else {
                            LinearLayout linearLayout = findViewById(R.id.linearlayout_media_player);

                            linearLayout.setBackgroundResource(R.drawable.main_background);
                            GradientDrawable gradientDrawableBg = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                    new int[]{0xff000000, 0xff000000});
                            linearLayout.setBackground(gradientDrawableBg);
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
        playThreadBtn();
        btnButtonLikeSong();
//        repeatOneThreadBtn();
//        shuffleThreadBtn();
        btnAddPlaylist();

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

    // PREPARE MEDIA PLAYER URL
    private void prepareMediaPlayer(String url) {
        Log.d("URL", url);
        try {
            musicService.setDataSource(url);
            musicService.prepare();
            txtTimeTotal.setText(milliSecondsToTimer(musicService.getDuration() - 1000));
        } catch (Exception ex) {
            Toast.makeText(PlayActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

//mp.setDataSource(context, uri);

    // UPDATE SEEK BAR
    private Runnable update = new Runnable() {
        @Override
        public void run() {
            if (isActivityVisible) {
                updateSeekBar();
                long currentDuration = musicService.getCurrentPosition();
                txtTimePlayed.setText(milliSecondsToTimer(currentDuration));
            }
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
        String secondString;
        int hours = (int) (milliSeconds / (1000 * 60 * 60));
//        int minutes = (int) (milliSeconds % (1000 * 60 * 60)) / (1000 * 60);
        int minutes = (int) (milliSeconds / 1000) / 60;
//        int seconds = (int) ((milliSeconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        int seconds = (int) (milliSeconds / 1000) % 60;

        if (hours > 0) {
            timeString = hours + ":";
        }
        if (seconds < 10) {
            secondString = "0" + seconds;
        } else {
            secondString = "" + seconds;
        }

        timeString = timeString + minutes + ":" + secondString;
//        timeString = timeString+m+":"+s;
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
     * CONNECT TO SERVICE
     */
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MusicService.MyBinder myBinder = (MusicService.MyBinder) service;
        musicService = myBinder.getService();
        musicService.setCallback(this);
        initComponent();
        playSongs();
        setEvents();
        if (user.getId() == 0) {
            btnLikeSong.setVisibility(View.GONE);
            btnAddPlaylist.setVisibility(View.GONE);
        } else {
            btnLikeSong.setVisibility(View.VISIBLE);
            btnAddPlaylist.setVisibility(View.VISIBLE);
        }
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
        isActivityVisible = false;
        unbindService(PlayActivity.this);
    }

    // ON RESUME
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
    }
}