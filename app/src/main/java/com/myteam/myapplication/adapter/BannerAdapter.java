package com.myteam.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.myteam.myapplication.R;
import com.myteam.myapplication.model.Song;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BannerAdapter extends PagerAdapter {
    Context context;
    ArrayList<Song> songList;

    //  Tạo contructor


    public BannerAdapter(Context context, ArrayList<Song> songList) {
        this.context = context;
        this.songList = songList;
    }

    // Muốn vẽ bao nhiêu pager?
    // = kích thước list
    @Override
    public int getCount() {
        return songList.size();
    }

    // true: sử dụng Object riêng
    // false: không sử dụng object riêng
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    // Object riêng chứa giao diện do người dùng tự định nghĩa
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.banner_dynamic, null);

        ImageView imageBackgroundBanner = view.findViewById(R.id.imageview_backround_banner);
        ImageView imgSongBanner = view.findViewById(R.id.imageview_banner);
        TextView txtTitleSongBanner = view.findViewById(R.id.textview_title_banner);

        txtTitleSongBanner.setText(songList.get(position).getName());
        return view;
    }
}
