package com.myteam.myapplication.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.myteam.myapplication.R;
import com.myteam.myapplication.activity.PlayActivity;
import com.myteam.myapplication.model.Song;
import com.myteam.myapplication.service.MusicService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CurrentPlaylistAdapter extends RecyclerView.Adapter<CurrentPlaylistAdapter.MyViewHolder> {
    private ArrayList<Song> mSongs;
    private LayoutInflater mLayoutInflater;
    private Activity mContext;
    private int mResource;

    public void updateList (ArrayList<Song> songs) {
        if (songs != null && songs.size() > 0) {
            mSongs.clear();
            mSongs.addAll(songs);
            notifyDataSetChanged();
        }
    }

    public CurrentPlaylistAdapter(Activity context, int resource, ArrayList<Song> songs) {
        this.mSongs = songs;
        this.mContext = context;
        this.mResource = resource;

        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CurrentPlaylistAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = mLayoutInflater.inflate(mResource, parent, false);
        return new CurrentPlaylistAdapter.MyViewHolder(item);
    }


    @Override
    public void onBindViewHolder(@NonNull CurrentPlaylistAdapter.MyViewHolder holder, final int position) {

        final Song song = mSongs.get(position);

        holder.txtSongName.setText(song.getName());

        holder.txtArtistName.setText(song.getArtistsName());

        if (song.isActive()) {
            holder.txtPlaying.setText("Đang Phát");
        }
        Picasso.with(mContext).load(song.getUrlImage()).into(holder.imgSongSquare);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MusicService.class);
                intent.putExtra("changeCurrentPosition", position);
                mContext.startService(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mSongs.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtSongName;
        public TextView txtArtistName;
        public TextView txtPlaying;
        public ImageView imgSongSquare;
        public CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSongName = itemView.findViewById(R.id.textview_song_name_current_playlist_item);
            txtArtistName = itemView.findViewById(R.id.textview_artist_name__current_playlist_item);
            txtPlaying = itemView.findViewById(R.id.textview_playing_current_playlist_item);
            imgSongSquare = itemView.findViewById(R.id.imageview_image_song_current_playlist_item);
            cardView = itemView.findViewById(R.id.cardview_current_playlist_song_item);
        }
    }
}
