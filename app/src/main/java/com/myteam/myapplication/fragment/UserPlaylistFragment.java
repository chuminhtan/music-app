package com.myteam.myapplication.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.myteam.myapplication.activity.ArtistActivity;
import com.myteam.myapplication.adapter.UserPlaylistAdapter;
import com.myteam.myapplication.adapter.UserPlaylistLikedAdapter;
import com.myteam.myapplication.data.UserPlaylistAsyncResponse;
import com.myteam.myapplication.data.UserPlaylistData;
import com.myteam.myapplication.model.Playlist;
import com.myteam.myapplication.model.User;

import java.util.ArrayList;

public class UserPlaylistFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    ArrayList<Playlist> playlists;
    UserPlaylistLikedAdapter userPlaylistAdapter;
    User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_playlist, container, false);
        recyclerView = view.findViewById(R.id.recyclerview_user_playlist);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        getUser();
        getUserPlaylist(user);
        return view;
    }

    public void getUserPlaylist(User user) {
        playlists = new UserPlaylistData().getPlaylistsbyId(user, new UserPlaylistAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Playlist> playlists) {

                userPlaylistAdapter = new UserPlaylistLikedAdapter(getActivity(), R.layout.user_playlist_item, playlists);
                recyclerView.setAdapter(userPlaylistAdapter);
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
