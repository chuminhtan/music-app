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

import com.myteam.myapplication.R;
import com.myteam.myapplication.adapter.UserAlbumAdapter;
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
    ListView listViewAlbum;
    ArrayList<Album> albums;
    UserAlbumAdapter userAlbumAdapter;
    User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_album, container, false);
        listViewAlbum = view.findViewById(R.id.listview_user_album);
        getUser();
        getUserAlbum(user);
        return  view;
    }
    public void getUserAlbum (User user) {
        albums = new UserAlbumData().getAlbumsbyId(user, new UserAlbumAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Album> albums) {
                userAlbumAdapter = new UserAlbumAdapter(getActivity(), R.layout.user_album_item, albums);
                listViewAlbum.setAdapter(userAlbumAdapter);
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
