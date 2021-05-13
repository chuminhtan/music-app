package com.myteam.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.myteam.myapplication.R;
import com.myteam.myapplication.activity.PlaylistDetailActivity;
import com.myteam.myapplication.model.Playlist;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


// File chuyển đổi BannerFragment thành các thành phần hiển thị màn hình

public class BannerAdapter extends PagerAdapter{
    Context context;
    ArrayList<Playlist> playlistNewest;
    ImageView imageBackgroundBanner;

    //  contructor
    public BannerAdapter(Context context, ArrayList<Playlist> playlistNewest) {
        this.context = context;
        this.playlistNewest = playlistNewest;
    }


    // Muốn vẽ bao nhiêu pager?
    // = kích thước list

    @Override
    public int getCount() {
        return playlistNewest.size();
    }


    // true: sử dụng Object riêng
    // false: không sử dụng object riêng

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    // Object riêng chứa giao diện do người dùng tự định nghĩa

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.banner_dynamic_new, null);

        imageBackgroundBanner = view.findViewById(R.id.imageview_background_banner);

        // Thư viện Picasso hỗ trợ get và hiển thị ảnh khi có url
        String url = playlistNewest.get(position).getImageUrl();
        Picasso.with(context).load(playlistNewest.get(position).getImageUrl2()).into(imageBackgroundBanner);
        container.addView(view);

        // Click event
        clickEvent(view, playlistNewest.get(position));


        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }


    // Xử lý sự kiện click
    public void clickEvent(View view, final Playlist playlist) {

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlaylistDetailActivity.class);
                intent.putExtra("playlist", playlist);
                context.startActivity(intent);
            }
        });
    }
}
