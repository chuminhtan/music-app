package com.myteam.myapplication.data;

import com.myteam.myapplication.model.Song;

import java.util.ArrayList;

public interface SongListAsyncResponse {
    void processFinished(ArrayList<Song> songList);
}
