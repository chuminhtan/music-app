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
import com.myteam.myapplication.model.Playlist;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BannerAdapter extends PagerAdapter {
    Context context;
    ArrayList<Playlist> playlistNewest;

    //  Tạo contructor


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

        View view = inflater.inflate(R.layout.banner_dynamic, null);

        ImageView imageBackgroundBanner = view.findViewById(R.id.imageview_background_banner);
        ImageView imgPlaylistBanner = view.findViewById(R.id.imageview_banner);
        TextView txtTitlePlaylistBanner = view.findViewById(R.id.textview_title_banner);
        TextView txtDesPlaylistBanner = view.findViewById(R.id.textview_des_playlist_banner);

        txtDesPlaylistBanner.setText((playlistNewest.get(position).getDes()));
        txtTitlePlaylistBanner.setText(playlistNewest.get(position).getName());

        // Thư viện Picasso hỗ trợ get và hiển thị ảnh khi có url
        String url = playlistNewest.get(position).getImageUrl();
        Picasso.with(context).load(playlistNewest.get(position).getImageUrl()).into(imageBackgroundBanner);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
