package com.myteam.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.myteam.myapplication.R;
import com.myteam.myapplication.activity.PlaylistDetailActivity;
import com.myteam.myapplication.data.UserPlaylistAsycnResponse;
import com.myteam.myapplication.data.UserPlaylistData;
import com.myteam.myapplication.model.Playlist;
import com.myteam.myapplication.model.Song;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserPlaylistAdapter extends RecyclerView.Adapter<UserPlaylistAdapter.MyViewHolder> {
    private ArrayList<Playlist> mplaylist;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private int mResource,songId;
    private String userName,result, message;

    public UserPlaylistAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Playlist> objects, String username,int SongId) {
        this.mplaylist = objects;
        this.mContext = context;
        this.mResource = resource;
        userName = username;
        songId = SongId;
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
    public UserPlaylistAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View item = mLayoutInflater.inflate(mResource, parent,false);
        Log.d("USER PLAYLIST ADAPTER", "parent: " + parent.toString() + "resource " + mResource + "id of recycle" + R.layout.playlist_bottom_sheet_item);
        return new UserPlaylistAdapter.MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UserPlaylistAdapter.MyViewHolder holder, int position) {
        final Playlist playlist = mplaylist.get(position);
        holder.txtPlaylistTitle.setText(playlist.getName());
        holder.txtUserName.setText(userName);
        Log.d("USER PLAYLIST ADAPTER", "Image Url " + playlist.getImageUrl());
        Picasso.with(mContext).load(playlist.getImageUrl()).into(holder.imgPlaylistImageSquare);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("USERPLAYLIST", "From UserPlaylistAdapter, SongId " + String.valueOf(songId) + "playlist id " + String.valueOf(playlist.getId()));
                addSongToPlaylist(songId,playlist.getId());
            }
        });
    }

    // Add song to user playlist
    public void addSongToPlaylist(int songId, int playlistId){
        new UserPlaylistData().addSongToPlaylist(songId, playlistId, new UserPlaylistAsycnResponse() {
            @Override
            public void processFinished(Map<String, String> mapResponse) {
                result = mapResponse.get("result");
                message = mapResponse.get("message");

                Log.d("USERPLAYLIST","From UserPlaylistAdapter-addSongToPlaylist Started");
                Log.d("USERPLAYLIST","From PUserPlaylistAdapter-addSongToPlaylist response : " + mapResponse.get("result") + " | " + mapResponse.get("message"));
                if(result.equalsIgnoreCase("success")) {
                    Log.d("USERPLAYLIST", "Add New User Playlist Successfully");
                    Toast.makeText(mContext,"Thêm bài hát vào playlist thành công", Toast.LENGTH_LONG).show();
                } else {
                    Log.d("USERPLAYLIST", "Add New User Playlist Unsuccesfully");
                    Toast.makeText(mContext,"Đã xảy ra lỗi. Không thể thêm bài hát vào Playlist", Toast.LENGTH_LONG).show();
                }
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
            Log.d("USER PLAYLIST ADAPTER", txtPlaylistTitle.toString());
            imgPlaylistImageSquare = itemView.findViewById(R.id.imageview_user_playlist_item);
            txtUserName = itemView.findViewById(R.id.textview_user_name_item);
            cardView = itemView.findViewById(R.id.cardview_user_playlist_item);
        }
    }
}


