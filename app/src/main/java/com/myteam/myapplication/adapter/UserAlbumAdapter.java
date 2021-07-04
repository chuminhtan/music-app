package com.myteam.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.myteam.myapplication.R;
import com.myteam.myapplication.activity.AlbumDetailActivity;
import com.myteam.myapplication.activity.PlaylistDetailActivity;
import com.myteam.myapplication.model.Album;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserAlbumAdapter extends ArrayAdapter<Album> {

    private int resourceLayout;
    private Context mContext;

    ImageView imgAlbum;
    TextView nameAlbum, artistAlbum;
    ImageButton btnListSongs;
    RelativeLayout relativeLayout;

    public UserAlbumAdapter(@NonNull Context context, int resource, @NonNull List<Album> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.resourceLayout = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(resourceLayout, null);
        }

        final Album album = getItem(position);

        if (album != null) {
            imgAlbum = convertView.findViewById(R.id.user_album_item_image);
            nameAlbum = convertView.findViewById(R.id.user_album_item_name);
            artistAlbum = convertView.findViewById(R.id.user_album_item_artist);
            btnListSongs = convertView.findViewById(R.id.btn_user_album_songs);
            relativeLayout = convertView.findViewById(R.id.user_album_item);

            Picasso.with(getContext()).load(album.getImageUrl()).into(imgAlbum);
            nameAlbum.setText(album.getName());

            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                Intent intent = new Intent(mContext, AlbumDetailActivity.class);
                intent.putExtra("album", album);
                mContext.startActivity(intent);
                }
            });
        }
        return convertView;
    }
}