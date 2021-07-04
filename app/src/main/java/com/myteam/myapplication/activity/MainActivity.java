package com.myteam.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.tabs.TabLayout;
import com.myteam.myapplication.R;
import com.myteam.myapplication.adapter.MainViewPagerAdapter;
import com.myteam.myapplication.data.SongData;
import com.myteam.myapplication.fragment.HomeFragment;
import com.myteam.myapplication.fragment.NoUserFragment;
import com.myteam.myapplication.fragment.OfflineFragment;
import com.myteam.myapplication.fragment.SearchFragment;
import com.myteam.myapplication.fragment.UserFragment;

public class MainActivity extends AppCompatActivity {

    public static boolean RELOAD_MENU_TAB = false;

    // Khai báo TabLayout cho menu

    private TabLayout tabLayout;
    private ViewPager viewPager;
    MainViewPagerAdapter mainViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mappingComponent();
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (RELOAD_MENU_TAB) {
            init();
            RELOAD_MENU_TAB = false;
        }
    }

    private void mappingComponent() {
    }

    private void init() {
        tabLayout = findViewById(R.id.myTabLayout);
        viewPager = findViewById(R.id.myViewPager);

        mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        // Thêm 3 Fragment vào bộ chuyển đổi mainView
        createTab();

        // Set Adapter cho view pager
        viewPager.setAdapter(mainViewPagerAdapter);

        // Set viewPager cho Tab layout
        tabLayout.setupWithViewPager(viewPager);

        // Thiết Lập Icon
        createIcon();
    }

    private void createTab() {
        mainViewPagerAdapter.addFragment(new HomeFragment(), "");
        mainViewPagerAdapter.addFragment(new SearchFragment(), "");
        mainViewPagerAdapter.addFragment(new UserFragment(), "");
        mainViewPagerAdapter.addFragment(new OfflineFragment(),"");
    }

    private void createIcon() {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_circle_dish);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_search_30);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_baseline_supervised_user_circle_50);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_library_music_30);
    }

}