package com.myteam.myapplication.data;

import com.myteam.myapplication.model.Album;

import java.util.ArrayList;

public interface UserAlbumAsyncResponse {
    void processFinished(ArrayList<Album> albums);
}
