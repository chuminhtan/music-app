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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PlayListSearchAdapter extends RecyclerView.Adapter<PlayListSearchAdapter.MyViewHolder> {
    private ArrayList<Song> mSongs;
    private LayoutInflater mLayoutInflater;
    private Activity mContext;
    private int mResource;

    public PlayListSearchAdapter(Activity context, int resource, ArrayList<Song> songs) {
        this.mSongs = songs;
        this.mContext = context;
        this.mResource = resource;

        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PlayListSearchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = mLayoutInflater.inflate(mResource, parent, false);
        return new PlayListSearchAdapter.MyViewHolder(item);
    }


    @Override
    public void onBindViewHolder(@NonNull PlayListSearchAdapter.MyViewHolder holder, int position) {

        final Song song = mSongs.get(position);

        holder.txtArtistName.setText(song.getArtistsName());

        Picasso.with(mContext).load(song.getUrlImage()).into(holder.imgSongSquare);

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
        public TextView txtArtistName;
        public ImageView imgSongSquare;
        public CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtArtistName = itemView.findViewById(R.id.textview_artistname_item);
            imgSongSquare = itemView.findViewById(R.id.imageview_songlist_item);
            cardView = itemView.findViewById(R.id.cardview_songlist_item);
        }
    }
}
