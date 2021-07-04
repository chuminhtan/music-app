package com.myteam.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.myteam.myapplication.R;
import com.myteam.myapplication.activity.EditUserInfoActivity;
import com.myteam.myapplication.activity.PlaylistDetailActivity;
import com.myteam.myapplication.model.Playlist;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserPlaylistAdapter extends ArrayAdapter<Playlist> {

    private int resourceLayout;
    private Context mContext;

    ImageView imgPlaylist;
    TextView namePlaylist;
    ImageButton btnlistsong;
    RelativeLayout relativeLayout;


    public UserPlaylistAdapter(@NonNull Context context, int resource, @NonNull List<Playlist> objects) {
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

        final Playlist playlist = getItem(position);

        if (playlist != null) {
            imgPlaylist = convertView.findViewById(R.id.user_playlist_item_image);
            namePlaylist = convertView.findViewById(R.id.user_playlist_item_name);
            btnlistsong = convertView.findViewById(R.id.btn_user_playlist_songs);
            relativeLayout = convertView.findViewById(R.id.user_playlist_item);

            Picasso.with(getContext()).load(playlist.getImageUrl()).into(imgPlaylist);
            namePlaylist.setText(playlist.getName());
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PlaylistDetailActivity.class);
                    intent.putExtra("playlist", playlist);
                    mContext.startActivity(intent);
                }
            });
        }

        return convertView;
    }
}