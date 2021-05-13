package com.myteam.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.myteam.myapplication.R;
import com.myteam.myapplication.activity.PlaylistDetailActivity;
import com.myteam.myapplication.model.Playlist;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PlaylistSquareAdapter extends RecyclerView.Adapter<PlaylistSquareAdapter.MyViewHolder> {

    private ArrayList<Playlist> mplaylist;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private int mResource;

    public PlaylistSquareAdapter(Context context, int resource,  ArrayList<Playlist> mplaylist) {
        this.mplaylist = mplaylist;
        this.mContext = context;
        this.mResource = resource;

        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = mLayoutInflater.inflate(mResource, parent,false);
        return new MyViewHolder(item);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Playlist playlist = mplaylist.get(position);
        holder.txtPlaylistTitle.setText(playlist.getName());
        Picasso.with(mContext).load(playlist.getImageUrl()).into(holder.imgPlaylistImageSquare);

        holder.imgPlaylistImageSquare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PlaylistDetailActivity.class);
                intent.putExtra("playlist", playlist);
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mplaylist.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView txtPlaylistTitle;
        public ImageView imgPlaylistImageSquare;
        public CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPlaylistTitle = itemView.findViewById(R.id.textview_title_playlist_square_item);
            imgPlaylistImageSquare = itemView.findViewById(R.id.imageview_playlist_square_item);
        }
    }


}
