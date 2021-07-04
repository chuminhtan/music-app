package com.myteam.myapplication.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.myteam.myapplication.R;
import com.myteam.myapplication.model.Playlist;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;

public class UserPlaylistLikedAdapter extends  RecyclerView.Adapter<UserPlaylistLikedAdapter.MyViewHolder>{
    private ArrayList<Playlist> mplaylist;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private int mResource,songId;

    public UserPlaylistLikedAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Playlist> objects) {
        this.mplaylist = objects;
        this.mContext = context;
        this.mResource = resource;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void updateList (ArrayList<Playlist> playlists) {
        if (playlists != null && playlists.size() > 0) {
            playlists.clear();
            playlists.addAll(playlists);
            notifyDataSetChanged();
        }
    }
    @NonNull
    @NotNull
    @Override
    public UserPlaylistAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = mLayoutInflater.inflate(mResource, parent,false);
        Log.d("USER PLAYLIST ADAPTER", "parent: " + parent.toString() + "resource " + mResource + "id of recycle" + R.layout.playlist_bottom_sheet_item);
        return item;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        final Playlist playlist = mplaylist.get(position);
        holder.txtPlaylistTitle.setText(playlist.getName());
        holder.txtUserName.setText(userName);

        Picasso.with(mContext).load(playlist.getImageUrl()).into(holder.imgPlaylistImageSquare);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("USERPLAYLIST", "From UserPlaylistAdapter, SongId " + String.valueOf(songId) + "playlist id " + String.valueOf(playlist.getId()));
                addSongToPlaylist(songId,playlist.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d("USER PLAYLIST ADAPTER", String.valueOf(mplaylist.size()));
        return mplaylist.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView txtPlaylistTitle;
        public ImageView imgPlaylistImageSquare;
        public TextView txtUserName;
        public CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPlaylistTitle = itemView.findViewById(R.id.textview_user_playlist_name_item);
            imgPlaylistImageSquare = itemView.findViewById(R.id.imageview_user_playlist_item);
            txtUserName = itemView.findViewById(R.id.textview_user_name_item);
            cardView = itemView.findViewById(R.id.cardview_user_playlist_item);
        }
    }
}
