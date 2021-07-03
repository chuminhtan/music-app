
/**
 * FRAGMENT BANNER HIỂN THỊ TRÊN TRANG HOME
 * */

package com.myteam.myapplication.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.myteam.myapplication.R;
import com.myteam.myapplication.adapter.BannerAdapter;
import com.myteam.myapplication.data.PlaylistArrayListAsyncResponse;
import com.myteam.myapplication.data.PlaylistData;
import com.myteam.myapplication.model.Playlist;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class BannerFragment extends Fragment {
    View view;
    ViewPager viewPager;
    CircleIndicator circleIndicator;
    BannerAdapter bannerAdapter;
    Handler handler;
    Runnable update;
    Timer timer;

    int currentItem = 0;

    private ArrayList<Playlist> playlistNewest;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_banner, container, false);

        mapping();

        getPlaylistNewest();

        return view;
    }

    private void mapping() {
        viewPager = view.findViewById(R.id.viewpager);
        circleIndicator = view.findViewById(R.id.indicatorDefault);
    }


    private void getPlaylistNewest() {
        playlistNewest = new PlaylistData().getNewUpload(new PlaylistArrayListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Playlist> playlistNewest) {
                bannerAdapter = new BannerAdapter(getActivity(), playlistNewest);
                viewPager.setAdapter(bannerAdapter);
                circleIndicator.setViewPager(viewPager);
                handler = new Handler();
                update = new Runnable() {
                    @Override
                    public void run() {
                        currentItem = viewPager.getCurrentItem();
                        if (currentItem == 4) {
                            currentItem = 0;
                        }
                        viewPager.setCurrentItem(currentItem++, true);
                    }
                };

                timer = new Timer(); // This will create a new Thread
                timer.schedule(new TimerTask() { // task to be scheduled
                    @Override
                    public void run() {
                        handler.post(update);
                    }
                }, 3000, 3000);
            }
        });
    }
}
