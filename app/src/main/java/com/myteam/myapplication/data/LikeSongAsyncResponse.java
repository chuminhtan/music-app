package com.myteam.myapplication.data;

import com.myteam.myapplication.model.Song;

import java.util.ArrayList;

public interface LikeSongAsyncResponse {
    void processFinished(ArrayList<Song> songs);
}
