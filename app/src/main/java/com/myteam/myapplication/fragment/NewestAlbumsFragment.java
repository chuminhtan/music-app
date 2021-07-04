package com.myteam.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myteam.myapplication.R;
import com.myteam.myapplication.activity.MoreAlbumActivity;
import com.myteam.myapplication.activity.MorePlaylistActivity;
import com.myteam.myapplication.adapter.AlbumAdapter;
import com.myteam.myapplication.data.AlbumData;
import com.myteam.myapplication.data.NewestAlbumsAsyncRespone;
import com.myteam.myapplication.model.Album;

import java.util.ArrayList;

public class NewestAlbumsFragment extends Fragment {
    private View view;
    private TextView txtListTitle; // Tên chủ đề
    private AlbumAdapter albumAdapter;
    private ArrayList<Album> albums = new ArrayList<>();
    private RecyclerView recyclerView;
    TextView tvShowMore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_newest_album, container, false);
        mapping();
        txtListTitle.setText("Album mới nhất");
        getData();
        tvShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MoreAlbumActivity.class);
                intent.putExtra("album", albums);
                startActivity(intent);
            }
        });
        return view;
    }

    public void mapping () {
        recyclerView = view.findViewById(R.id.recyclerview_album_horizontal);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        txtListTitle = view.findViewById(R.id.textview_title_album_horizontal);
        tvShowMore = view.findViewById(R.id.textview_more_newest_album);
    }

    public void getData() {
        new AlbumData().getNewestAlbums(new NewestAlbumsAsyncRespone() {
            @Override
            public void processFinished(ArrayList<Album> albums) {
                albums = albums;
                albumAdapter = new AlbumAdapter(getActivity(), R.layout.album_square_item, albums);
                recyclerView.setAdapter(albumAdapter);
            }
        });
    }
}
