package com.myteam.myapplication.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myteam.myapplication.R;
import com.myteam.myapplication.adapter.UserAlbumAdapter;
import com.myteam.myapplication.adapter.UserAlbumLikedAdapter;
import com.myteam.myapplication.adapter.UserPlaylistAdapter;
import com.myteam.myapplication.data.UserAlbumAsyncResponse;
import com.myteam.myapplication.data.UserAlbumData;
import com.myteam.myapplication.data.UserPlaylistAsyncResponse;
import com.myteam.myapplication.data.UserPlaylistData;
import com.myteam.myapplication.model.Album;
import com.myteam.myapplication.model.Playlist;
import com.myteam.myapplication.model.User;

import java.util.ArrayList;

public class UserAlbumFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    ArrayList<Album> albums;
    UserAlbumLikedAdapter userAlbumAdapter;
    User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_album, container, false);
        recyclerView = view.findViewById(R.id.recyclerview_user_album);
        getUser();
        getUserAlbum(user);
        return  view;
    }
    public void getUserAlbum (User user) {
        albums = new UserAlbumData().getAlbumsbyId(user, new UserAlbumAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Album> albums) {
                userAlbumAdapter = new UserAlbumLikedAdapter(getActivity(), R.layout.user_album_item, albums);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(userAlbumAdapter);
            }
        });
    }

    private void getUser() {
        user = new User();
        SharedPreferences sharedPref = getContext().getSharedPreferences("USER", Context.MODE_PRIVATE);
        user.setId(sharedPref.getInt("user_id", -1));
        user.setName(sharedPref.getString("user_name", ""));
        user.setEmail(sharedPref.getString("user_email", ""));
    }
}
