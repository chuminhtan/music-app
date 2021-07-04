/**
 * TRANG SEARCH
 */


package com.myteam.myapplication.fragment;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.myteam.myapplication.R;
import com.myteam.myapplication.adapter.ListSongBySearchAdapter;

public class SearchFragment extends Fragment {
    public static String WORD = "";
    EditText editext_search_field;
    ImageView image_search;
    TabLayout tabLayout;
    ViewPager viewPager;
    TabItem tabSongs, tabPlaylists, tabArtists;
    ListSongBySearchAdapter listSongBySearchAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        editext_search_field = v.findViewById(R.id.edit_search_field);
        image_search = v.findViewById(R.id.imageview_search);

        tabLayout = v.findViewById(R.id.tabbar);
        tabLayout.setVisibility(View.INVISIBLE);
        tabSongs = v.findViewById(R.id.tabSongs);
        tabPlaylists = v.findViewById(R.id.tabPlaylist);
        tabArtists = v.findViewById(R.id.tabArtists);
        viewPager = v.findViewById(R.id.ViewPager);

        listSongBySearchAdapter = new ListSongBySearchAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());

        image_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WORD = editext_search_field.getText().toString();
                tabLayout.setVisibility(View.VISIBLE);
                viewPager.setAdapter(listSongBySearchAdapter);
                viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        viewPager.setCurrentItem(tab.getPosition());
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
            }
        });
        editext_search_field.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER) {

                }
                return false;
            }
        });
        return v;
    }

}