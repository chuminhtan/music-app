package com.myteam.myapplication.data;

import com.myteam.myapplication.model.Playlist;
import com.myteam.myapplication.model.Song;

import java.util.ArrayList;

public interface PlaylistArrayListAsyncResponse {
    void processFinished(ArrayList<Playlist> playlistArrayList);
}
