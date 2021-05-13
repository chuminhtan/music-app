package com.myteam.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myteam.myapplication.R;
import com.myteam.myapplication.model.Playlist;
import com.myteam.myapplication.model.Song;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SonglistAdapter  extends RecyclerView.Adapter<SonglistAdapter.MyViewHolder> {
    private ArrayList<Song> mSongs;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private int mResource;

    public SonglistAdapter(Context context, int resource,  ArrayList<Song> songs) {
        this.mSongs = songs;
        this.mContext = context;
        this.mResource = resource;

        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public SonglistAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = mLayoutInflater.inflate(mResource, parent,false);
        return new SonglistAdapter.MyViewHolder(item);
    }


    @Override
    public void onBindViewHolder(@NonNull SonglistAdapter.MyViewHolder holder, int position) {

        Song song = mSongs.get(position);

        holder.txtSongName.setText(song.getName());

        String artistsName = song.getArtists().get(0).getName();
        for (int i = 1; i < song.getArtists().size(); i++) {
                artistsName += ", " + song.getArtists().get(i).getName();
        }
        holder.txtArtistName.setText(artistsName);

        Picasso.with(mContext).load(song.getUrlImage()).into(holder.imgSongSquare);
    }


    @Override
    public int getItemCount() {
        return mSongs.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView txtSongName;
        public TextView txtArtistName;
        public ImageView imgSongSquare;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtSongName = itemView.findViewById(R.id.textview_songname_item);
            txtArtistName = itemView.findViewById(R.id.textview_artistname_item);
            imgSongSquare = itemView.findViewById(R.id.imageview_songlist_item);
        }
    }
}
