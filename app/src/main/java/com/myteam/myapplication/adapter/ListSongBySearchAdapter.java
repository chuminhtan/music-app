package com.myteam.myapplication.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.myteam.myapplication.R;
import com.myteam.myapplication.activity.PlayMusicActivity;
import com.myteam.myapplication.model.Song;
import com.myteam.myapplication.util.ServerInfo;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class ListSongBySearchAdapter extends ArrayAdapter<Song> {

    private Activity context;

    public ListSongBySearchAdapter(Activity context, int layoutID, List<Song> objects) {
        super(context, layoutID, objects);
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_vertical_white_text, null, false);
        }
        // Get item
        Song song = getItem(position);

        //Get view
        ImageView vert_item_image = convertView.findViewById(R.id.vert_item_image);
        TextView vert_item_song = convertView.findViewById(R.id.vert_item_song);
        TextView vert_item_artist = convertView.findViewById(R.id.vert_item_artist);

        // Set fullname
        if (song.getName() != null) {
            vert_item_song.setText((song.getName()));
        } else {
            vert_item_song.setText("");
        }

        // Set Artist
        if (song.getArtist() != null) {
            vert_item_artist.setText(song.getArtist().getName());
        } else {
            vert_item_artist.setText("");
        }

        // Set image
        String url_img = ServerInfo.SERVER_BASE + "/" + ServerInfo.STORAGE_SONG_IMG + "/" + song.getImg();
        Log.d("IMG", url_img);
        Picasso.with(getContext()).load(url_img).into(vert_item_image);
        Picasso.with(getContext()).setLoggingEnabled(true);
        return convertView;
    }
}
