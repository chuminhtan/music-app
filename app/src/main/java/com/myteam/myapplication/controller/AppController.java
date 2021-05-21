

package com.myteam.myapplication.controller;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
// INCLUDE
// VOLLEY LIBRARY

public class AppController extends Application {
    public static final String TAG = com.myteam.myapplication.controller.AppController.class.getSimpleName();
    private static com.myteam.myapplication.controller.AppController mInstance;
    private RequestQueue mRequestQueue;

    public static synchronized com.myteam.myapplication.controller.AppController getInstance() {
//        if (mInstance == null) {
//            mInstance = new AppController();
//        }//added this myself
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        createNotificationChannel();
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

//    public ImageLoader getImageLoader() {
//        getRequestQueue();
//        if (mImageLoader == null) {
//            mImageLoader = new ImageLoader(this.mRequestQueue,
//                    new LruBitmapCache());
//        }
//        return this.mImageLoader;
//    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }





    /**
     * **************************************************************
     * SERVICE
     *
     * MUSIC SERVICE HERE
     * *************************************************************
     * */

    public static final String CHANNEL_ID_1 = "channel1";
    public static final String CHANNEL_ID_2 = "channel2";
    public static final String ACTION_PREVIOUS = "actionprevious";
    public static final String ACTION_NEXT = "actionnext";
    public static final String ACTION_PLAY = "actionplay";
    public static final String ACTION_GOBACK = "actiongoback";

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 =
                    new NotificationChannel(
                            CHANNEL_ID_1,
                            "Channel(1)",
                            NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("Channel 1 Desc...");

            NotificationChannel channel2 =
                    new NotificationChannel(
                            CHANNEL_ID_2,
                            "Channel(2)",
                            NotificationManager.IMPORTANCE_HIGH
                    );

            channel2.setDescription("Channel 2 Desc...");
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel1);
            notificationManager.createNotificationChannel(channel2);


        }
    }
}

