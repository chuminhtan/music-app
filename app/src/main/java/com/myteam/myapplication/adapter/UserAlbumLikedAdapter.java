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
import com.myteam.myapplication.activity.PlaylistDetailActivity;
import com.myteam.myapplication.model.Album;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserAlbumLikedAdapter extends RecyclerView.Adapter<UserAlbumLikedAdapter.MyViewHolder>{

    private ArrayList<Album> mAlbums;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private int mResource;

    public UserAlbumLikedAdapter(Context context, int resource,  ArrayList<Album> malbums) {
        this.mAlbums = malbums;
        this.mContext = context;
        this.mResource = resource;

        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public UserAlbumLikedAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = mLayoutInflater.inflate(mResource, parent,false);
        return new UserAlbumLikedAdapter.MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAlbumLikedAdapter.MyViewHolder holder, int position) {
        final Album album = mAlbums.get(position);
        holder.txtAlbumTitle.setText(album.getName());
        Picasso.with(mContext).load(album.getImageUrl()).into(holder.imgAlbumImageSquare);

        holder.imgAlbumImageSquare.setOnClickListener(new View.OnClickListener() {
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
        public TextView txtAlbumTitle;
        public ImageView imgAlbumImageSquare;
        public CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAlbumTitle = itemView.findViewById(R.id.textview_title_user_album_item);
            imgAlbumImageSquare = itemView.findViewById(R.id.imageview_user_album_item);
        }
    }
}