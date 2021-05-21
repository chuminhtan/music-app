package com.myteam.myapplication.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.myteam.myapplication.service.MusicService;

import static com.myteam.myapplication.controller.AppController.ACTION_GOBACK;
import static com.myteam.myapplication.controller.AppController.ACTION_NEXT;
import static com.myteam.myapplication.controller.AppController.ACTION_PLAY;
import static com.myteam.myapplication.controller.AppController.ACTION_PREVIOUS;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String actionName = intent.getAction();
        Intent serviceIntent = new Intent(context, MusicService.class);
        if (actionName != null) {
            switch (actionName) {
                case ACTION_PLAY:
                    serviceIntent.putExtra("ActionName", "playPause");
                    context.startService(serviceIntent);
                    break;

                case ACTION_NEXT:
                    serviceIntent.putExtra("ActionName", "next");
                    context.startService(serviceIntent);
                    break;

                case ACTION_PREVIOUS:
                    serviceIntent.putExtra("ActionName", "previous");
                    context.startService(serviceIntent);
                    break;

                case ACTION_GOBACK:
                    serviceIntent.putExtra("ActionName", "goback");
                    context.startService(serviceIntent);
                    break;
            }
        }
    }
}
