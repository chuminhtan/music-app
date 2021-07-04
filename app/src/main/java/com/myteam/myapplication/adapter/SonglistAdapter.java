package com.myteam.myapplication.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.myteam.myapplication.R;
import com.myteam.myapplication.activity.PlayActivity;
import com.myteam.myapplication.model.Song;
import com.myteam.myapplication.util.ServerInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SonglistAdapter extends RecyclerView.Adapter<SonglistAdapter.MyViewHolder> {
    private ArrayList<Song> mSongs;
    private LayoutInflater mLayoutInflater;
    private Activity mContext;
    private int mResource;

    public SonglistAdapter(Activity context, int resource, ArrayList<Song> songs) {
        this.mSongs = songs;
        this.mContext = context;
        this.mResource = resource;

        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public SonglistAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = mLayoutInflater.inflate(mResource, parent, false);
        return new SonglistAdapter.MyViewHolder(item);
    }


    @Override
    public void onBindViewHolder(@NonNull SonglistAdapter.MyViewHolder holder, int position) {

        final Song song = mSongs.get(position);

        holder.txtSongName.setText(song.getName());

        holder.txtArtistName.setText(song.getArtistsName());

        if (song.getImg().isEmpty()) {
            Picasso.with(mContext).load(ServerInfo.SERVER_BASE +"/storage/music.png").into(holder.imgSongSquare);
        } else{
            Picasso.with(mContext).load(song.getUrlImage()).into(holder.imgSongSquare);
        }



        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PlayActivity.class);
                intent.putExtra("song", song);
                mContext.startActivity(intent);
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
        public ImageView imgSongSquare;
        public CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSongName = itemView.findViewById(R.id.textview_songname_item);
            txtArtistName = itemView.findViewById(R.id.textview_artistname_item);
            imgSongSquare = itemView.findViewById(R.id.imageview_songlist_item);
            cardView = itemView.findViewById(R.id.cardview_songlist_item);
        }
    }
}
