package com.myteam.myapplication.adapter;

import android.app.Activity;
import android.content.Context;

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
import com.myteam.myapplication.activity.ArtistActivity;
import com.myteam.myapplication.activity.PlaylistDetailActivity;
import com.myteam.myapplication.model.Artist;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ArtistAdapter extends  RecyclerView.Adapter<ArtistAdapter.MyViewHolder>{
    private ArrayList<Artist> martists;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private int mResource;

    public ArtistAdapter(Context context, int resource, ArrayList<Artist> artists) {
        this.mContext = context;
        this.mResource = resource;
        this.martists = artists;

        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ArtistAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = mLayoutInflater.inflate(mResource, parent, false);
        return new ArtistAdapter.MyViewHolder(item);
    }


    @Override
    public void onBindViewHolder(@NonNull ArtistAdapter.MyViewHolder holder, int position) {
        final Artist artist = martists.get(position);
        Picasso.with(mContext).load(artist.getUrlImg()).into(holder.imgArtistCircle);
        holder.txtArtistName.setText(artist.getName());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ArtistActivity.class);
                intent.putExtra("artist", artist);

                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return martists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtArtistName;
        public ImageView imgArtistCircle;
        public CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtArtistName = itemView.findViewById(R.id.textview_name_artist_item);
            imgArtistCircle = itemView.findViewById(R.id.imageview_artist_item);
            cardView = itemView.findViewById(R.id.carview_artist_item);
        }
    }
}
