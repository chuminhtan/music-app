package com.myteam.myapplication.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.myteam.myapplication.R;
import com.myteam.myapplication.activity.ActionPlaying;
import com.myteam.myapplication.activity.PlayActivity;
import com.myteam.myapplication.controller.NotificationReceiver;
import com.myteam.myapplication.model.Song;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.ArrayList;

import static com.myteam.myapplication.controller.AppController.ACTION_NEXT;
import static com.myteam.myapplication.controller.AppController.ACTION_PLAY;
import static com.myteam.myapplication.controller.AppController.ACTION_PREVIOUS;
import static com.myteam.myapplication.controller.AppController.CHANNEL_ID_2;

public class MusicService extends Service{

    IBinder mBider = new MyBinder();
    MediaPlayer mediaPlayer;
    ArrayList<Song> songlist = new ArrayList<>();
    int position = 0;
    ActionPlaying actionPlaying;
    MediaSessionCompat mediaSessionCompat;

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
        mediaSessionCompat = new MediaSessionCompat(getBaseContext(), "My Audio");

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
        // From NotificationReceiver
        String actionName =  intent.getStringExtra("ActionName");
        if (actionName != null) {
            switch(actionName) {
                case "playPause":
                    Toast.makeText(this, "PlayPause", Toast.LENGTH_SHORT ).show();
                    Log.d("Inside", "Action");
                    if (actionPlaying != null) {
                        actionPlaying.btnPlayPauseClicked();
                    }

                    break;

                case "next":
                    Toast.makeText(this, "Next", Toast.LENGTH_SHORT ).show();
                    Log.d("Inside", "Action");
                    if (actionPlaying != null) {
                        actionPlaying.btnSkipNextClicked();
                    }

                    break;

                case "previous":
                    Toast.makeText(this, "Previous", Toast.LENGTH_SHORT ).show();
                    Log.d("Inside", "Action");
                    if (actionPlaying!= null) {
                        actionPlaying.btnSkipPreClicked();
                    }

                    break;
            }
        }

        // From PlayActivity

        if (intent.hasExtra("songlist")) {
            songlist = (ArrayList<Song>) intent.getSerializableExtra("songlist");

        }


        int currentPositionOfAct = intent.getIntExtra("servicePosition", -1);

        if (currentPositionOfAct != -1) {
            position = currentPositionOfAct;
            showNotification(R.drawable.ic_play_circle);
        }

        if (intent.hasExtra("isPause")) {
            boolean isPause = intent.getBooleanExtra("isPause", false);
            if (isPause) {
             showNotification(R.drawable.ic_pause_circle);

            } else {
                showNotification(R.drawable.ic_play_circle);
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

    public void onCompleted(final boolean isShuffle) {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                if (actionPlaying != null) {
                    actionPlaying.btnSkipNextClicked();
                }
            }
        });

    }

    public void setCallback(ActionPlaying actionPlaying) {
        this.actionPlaying = actionPlaying;
    }


    /**
     * NOTIFICATION
     * */
    public void showNotification(final int playPauseBtn) {
        Intent intent = new Intent(this, PlayActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Intent prevIntent = new Intent(this, NotificationReceiver.class)
                .setAction(ACTION_PREVIOUS);
        final PendingIntent prevPending = PendingIntent.getBroadcast(this, 0, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent pauseIntent = new Intent(this, NotificationReceiver.class)
                .setAction(ACTION_PLAY);
        final PendingIntent pausePending = PendingIntent.getBroadcast(this, 0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent nextIntent = new Intent(this, NotificationReceiver.class)
                .setAction(ACTION_NEXT);
        final PendingIntent nextPending = PendingIntent.getBroadcast(this, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        final Song song = songlist.get(position);
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID_2)
                        .setSmallIcon(playPauseBtn)
                        .setLargeIcon(bitmap)
                        .setContentTitle(song.getName())
                        .setContentText(song.getArtistsName())
                        .addAction(R.drawable.ic_skip_previous, "Previous", prevPending)
                        .addAction(playPauseBtn, "Pause", pausePending)
                        .addAction(R.drawable.ic_skip_next, "Next", nextPending)
                        .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                                .setMediaSession(mediaSessionCompat.getSessionToken()))
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setOnlyAlertOnce(true)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .build();

//                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                notificationManager.notify(0, notification);
                        startForeground(2, notification);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(getApplicationContext()).load(song.getUrlImage()).into(target);
    }
}
