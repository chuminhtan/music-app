package com.myteam.myapplication.data;

import com.myteam.myapplication.model.Playlist;

import java.util.ArrayList;

public interface UserCreatedPlaylistAsyncResponse {
    void processFinished(ArrayList<Playlist> playlists);
}
