package com.myteam.myapplication.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.myteam.myapplication.activity.PlayActivity;
import com.myteam.myapplication.model.Song;

import java.util.ArrayList;

public class MusicService extends Service {

    IBinder mBider = new MyBinder();
    MediaPlayer mediaPlayer;
    ArrayList<Song> songlist = new ArrayList<>();

    // MY BINDER CLASS
    public class MyBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }



    // CONTRUCTOR
    public MusicService() {
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
        return super.onStartCommand(intent, flags, startId);
    }

    // MEDIA PLAYER COMMON METHOD
    void start(){
        mediaPlayer.start();
    }

    boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    void stop() {
        mediaPlayer.stop();
    }

    void release() {
        mediaPlayer.release();
    }

    int getDuration() {
        return mediaPlayer.getDuration();
    }

    void seekTo (int position) {
        mediaPlayer.seekTo(position);
    }

    
}
