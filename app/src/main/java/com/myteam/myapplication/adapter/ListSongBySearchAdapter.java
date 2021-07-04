package com.myteam.myapplication.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import com.myteam.myapplication.fragment.ArtistFragment;
import com.myteam.myapplication.fragment.PlaylistsFragment;
import com.myteam.myapplication.fragment.SongFragment;


import org.jetbrains.annotations.NotNull;


public class ListSongBySearchAdapter extends FragmentPagerAdapter {

    private int numberOfTabs;
    public ListSongBySearchAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }
    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new SongFragment();
            case 1:
                return new PlaylistsFragment();
            case 2:
                return new ArtistFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
