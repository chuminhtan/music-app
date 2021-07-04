package com.myteam.myapplication.data;

import com.myteam.myapplication.model.AlbumSong;
import com.myteam.myapplication.model.Artist;

import java.util.ArrayList;

public interface AlbumSongAsyncRespone {
    void processFinished(AlbumSong albumSong, ArrayList<Artist> artist);
}
