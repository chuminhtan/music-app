
package com.myteam.myapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.myteam.myapplication.R;
import com.myteam.myapplication.model.Playlist;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PlaylistAdapter extends ArrayAdapter<Playlist> {

    private int resourceLayout;
    private Context mContext;

    TextView txtNamePlaylist;
    ImageView imgBackground, imgPlaylist;

    public PlaylistAdapter(@NonNull Context context, int resource, @NonNull List<Playlist> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.resourceLayout = resource;
    }

    // Tạo viewholder để giữ lại khung view cho những lần sau app khởi chạy
//    class ViewHolder {
//        TextView txtNamePlaylist;
//        ImageView imgBackground, imgPlaylist;
//    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            // chuyển đổi từ xml -> view - hệ thống mới xài ddc
            LayoutInflater inflater = LayoutInflater.from(mContext);
            v = inflater.inflate(resourceLayout, null);
        }

//        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
//        View view = layoutInflater.inflate(R.layout.playlist_dynamic, null);

        // getItem định nghĩa sẵn: dùng để lấy phần tử từ List ra
        Playlist playlist = getItem(position);

        if (playlist != null) {

            txtNamePlaylist = v.findViewById(R.id.textview_name_playlist);
            imgPlaylist = v.findViewById(R.id.imageview_playlist);
            imgBackground = v.findViewById(R.id.imageview_background_playlist);

            String url = playlist.getImageUrl();

            // Picasso load ảnh từ server
            Picasso.with(getContext()).load(playlist.getImageUrl()).into(imgBackground);
            Picasso.with(getContext()).load(playlist.getImageUrl2()).into(imgPlaylist);
            txtNamePlaylist.setText(playlist.getName());
        }

        return v;
    }
}
