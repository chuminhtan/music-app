package com.myteam.myapplication.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.myteam.myapplication.activity.ActionPlaying;
import com.myteam.myapplication.activity.PlayActivity;
import com.myteam.myapplication.model.Song;

import java.io.IOException;
import java.util.ArrayList;

public class MusicService extends Service implements MediaPlayer.OnCompletionListener {

    IBinder mBider = new MyBinder();
    MediaPlayer mediaPlayer;
    ArrayList<Song> songlist = new ArrayList<>();
    int position = -1;
    ActionPlaying actionPlaying;

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener onCompletionListener) {

    }


    // MY BINDER CLASS
    public class MyBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }


    // CONTRUCTOR
    public MusicService() {
    }


    // ON CREATE

    @Override
    public void onCreate() {
        super.onCreate();
        songlist = PlayActivity.SONGLIST;
    }

    // ON BIND
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.e("Bind", "Method");
        return this.mBider;

    }


    // ON START COMMAND
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int myPosition = intent.getIntExtra("servicePosition", -1);
        if (myPosition != -1) {
            try {
                playMedia(myPosition);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return START_STICKY;

    }

    // PLAY MEDIA
    private void playMedia(int startPosistion) throws IOException {
        songlist = PlayActivity.SONGLIST;
        position = startPosistion;

        Song song = songlist.get(position);
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();

            if (songlist != null) {
                createMediaPlayer();
                mediaPlayer.setDataSource(song.getUrlSrc());
                mediaPlayer.prepare();
                mediaPlayer.start();
            }
        } else {
            createMediaPlayer();
            mediaPlayer.setDataSource(song.getUrlSrc());
            mediaPlayer.prepare();
            mediaPlayer.start();
        }

    }


    // MEDIA PLAYER COMMON METHOD
    public void start() {
        mediaPlayer.start();
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void stop() {
        mediaPlayer.stop();
    }

    public void release() {
        mediaPlayer.release();
    }

    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    public void seekTo(int position) {
        mediaPlayer.seekTo(position);
    }

    public void createMediaPlayer() {
        mediaPlayer = new MediaPlayer();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public void setDataSource(String url) throws IOException {
        mediaPlayer.setDataSource(url);
    }

    public void prepare() throws IOException {
        mediaPlayer.prepare();
    }

    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public void reset() {
        mediaPlayer.reset();
    }

    public void onCompleted() {
        mediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (actionPlaying != null) {
            actionPlaying.btnSkipNextClicked();
        }
        createMediaPlayer();
        mediaPlayer.start();
        this.onCompleted();

    }
}
