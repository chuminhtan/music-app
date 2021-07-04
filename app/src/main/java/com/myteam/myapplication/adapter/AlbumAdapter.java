package com.myteam.myapplication.adapter;

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
import com.myteam.myapplication.activity.AlbumDetailActivity;
import com.myteam.myapplication.activity.ArtistActivity;
import com.myteam.myapplication.model.Album;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyViewHolder> {
    private ArrayList<Album> mAlbums;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private int mResource;

    public AlbumAdapter(Context context, int resource, ArrayList<Album> albums){
        this.mContext = context;
        this.mResource = resource;
        this.mAlbums = albums;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = mLayoutInflater.inflate(mResource, parent, false);
        return new AlbumAdapter.MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Album album = mAlbums.get(position);
        holder.txtAlbumTitle.setText(album.getName());
        Picasso.with(mContext).load(album.getImageUrl()).into(holder.imgAlbum);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AlbumDetailActivity.class);
                intent.putExtra("album", album);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAlbums.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public ImageView imgAlbum;
        public TextView txtAlbumTitle;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.carview_album_square_item);
            imgAlbum = itemView.findViewById(R.id.imageview_album_square_item);
            txtAlbumTitle = itemView.findViewById(R.id.textview_title_album_square_item);
        }
    }
}
