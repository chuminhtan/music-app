package com.myteam.myapplication.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;



// File chuyển đổi các thành phần dữ liệu trong fragment để hiển thị được trên màn hình

public class MainViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> arrayFragment = new ArrayList<>();
    private ArrayList<String> arrayTitle = new ArrayList<>();

    public MainViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return arrayFragment.get(position);
    }

    @Override
    public int getCount() {
        return arrayFragment.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return arrayTitle.get(position);
    }

    @Override
    public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
        super.destroyItem(container, position, object);
    }

    // ADD FRAGMENT
    public void addFragment(Fragment fragment, String title) {
        arrayFragment.add(fragment);
        arrayTitle.add(title);
    }


    // REMOVE ALL FRAGMENT
    public void removeAllFragment() {
        arrayFragment.clear();
    }
    // REPLACE FRAGMENT
    public void replaceFragment(Fragment fragment, int position) {
        arrayFragment.set(position,fragment);
    }
}
