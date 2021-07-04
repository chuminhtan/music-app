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
import com.myteam.myapplication.model.Artist;
import com.myteam.myapplication.model.Song;
import com.myteam.myapplication.util.ServerInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.MyViewHolder> {
    private List<Artist> mArtists;
    private LayoutInflater mLayoutInflater;
    private Activity mContext;
    private int mResource;

    public ArtistAdapter(Activity context, int resource, List<Artist> artists) {
        this.mArtists = artists;
        this.mContext = context;
        this.mResource = resource;

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

        final Artist artist = mArtists.get(position);

        holder.txtArtistName.setText(artist.getName());

        Picasso.with(mContext).load(ServerInfo.SERVER_BASE + "/" + ServerInfo.STORAGE_ARTIST + "/" + artist.getImg()).into(holder.imageArtistView);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PlayActivity.class);
                intent.putExtra("song", artist);
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        if(mArtists != null) {
            return mArtists.size();
        } else {
            return 0;
        }

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtArtistName;
        public ImageView imageArtistView;
        public CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtArtistName = itemView.findViewById(R.id.textview_title_artist_item);
            imageArtistView = itemView.findViewById(R.id.imageview_artist_item);
            cardView = itemView.findViewById(R.id.cardview_artist_item);
        }
    }
}
