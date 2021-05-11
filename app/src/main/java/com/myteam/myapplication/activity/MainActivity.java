package com.myteam.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.myteam.myapplication.R;
import com.myteam.myapplication.adapter.MainViewPagerAdapter;
import com.myteam.myapplication.data.SongData;
import com.myteam.myapplication.fragment.HomeFragment;
import com.myteam.myapplication.fragment.SearchFragment;
import com.myteam.myapplication.fragment.UserFragment;

public class MainActivity extends AppCompatActivity {

    // Khai báo TabLayout cho menu
    private TabLayout tabLayout;
    // Khai báo View Pager
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ thành phần ra View
        mappingComponent();

        // Khởi tạo menu tab
        init();
    }

    // Khởi tạo ViewPager chứa các Fragment
    private void init() {
        MainViewPagerAdapter mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager()); // Truyền vào Fragment Quản lý tại Activity
        mainViewPagerAdapter.addGragment(new HomeFragment(), "Trang Chủ");
        mainViewPagerAdapter.addGragment(new SearchFragment(), "Tìm Kiếm");
        mainViewPagerAdapter.addGragment(new UserFragment(), "Người Dùng");

        // Set Adapter cho view pager
        viewPager.setAdapter(mainViewPagerAdapter);

        // Set viewPager cho Tab layout
        tabLayout.setupWithViewPager(viewPager);

        // Thiết Lập Icon
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_home_30);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_search_30);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_baseline_supervised_user_circle_50);
    }


    // Ánh Xạ Các Thành Phần Từ MainActivity Đến View
    private void mappingComponent() {
        tabLayout = findViewById(R.id.myTabLayout);
        viewPager = findViewById(R.id.myViewPager);
    }
}