package com.myteam.myapplication.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.myteam.myapplication.R;
import com.myteam.myapplication.data.PlaylistArrayListAsyncResponse;
import com.myteam.myapplication.data.PlaylistData;
import com.myteam.myapplication.data.UserCreatedPlaylistAsyncResponse;
import com.myteam.myapplication.data.UserCreatedPlaylistData;
import com.myteam.myapplication.data.UserData;
import com.myteam.myapplication.model.Playlist;
import com.myteam.myapplication.model.Song;
import com.myteam.myapplication.model.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PlaylistBottomSheetAdapter extends BottomSheetDialogFragment {
    private ImageView btn_add_playlist;
    private RecyclerView recyclerView_user_playlist;
    private UserPlaylistAdapter userPlaylistAdapter;
    private CardView cardView;
    private User user;
    private int songId;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.playlist_bottom_sheet_layout, container, false);
        Bundle bundle = getArguments();
        songId = bundle.getInt("song_id");
        Log.d("USERPLAYLIST", "From PlaylistBottomSheetAdapter, SongId" + String.valueOf(songId));
        btn_add_playlist = v.findViewById(R.id.imageview_add_playlist);
        recyclerView_user_playlist = v.findViewById(R.id.recyclerview_user_playlist);
        cardView = v.findViewById(R.id.cardview_add_user_playlist);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
        mapping();
        getPreferences();
        getData();
        return v;
    }


    public void mapping() {
        // Set Grid - hiển thị lưới cho từng item
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView_user_playlist.setLayoutManager(mLayoutManager);
    }

    public void openDialog() {
        CreateUserPlaylistDialog createUserPlaylistDialog = new CreateUserPlaylistDialog();
        createUserPlaylistDialog.show(getActivity().getSupportFragmentManager(), "Tạo Playlist");
    }

    //Get UserID
    private void getPreferences() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("USER", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", 0);
        String userName = sharedPreferences.getString("user_name", "");
        String userEmail = sharedPreferences.getString("user_email", "");
        Log.d("Playlist BottomSheet", "User Information: " + String.valueOf(userId) + " " + userName + " " + userEmail);
        user = new User(userId, userName, userEmail, "");
    }

    public void getData() {
        new UserCreatedPlaylistData().getPlaylistsbyId(user, new UserCreatedPlaylistAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Playlist> playlists) {
                userPlaylistAdapter = new UserPlaylistAdapter(getActivity(), R.layout.playlist_bottom_sheet_item, playlists, user.getName(), songId);
                recyclerView_user_playlist.setAdapter(userPlaylistAdapter);
            }
        });
    }
}
