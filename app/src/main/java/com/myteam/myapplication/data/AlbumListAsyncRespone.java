package com.myteam.myapplication.data;

import com.myteam.myapplication.model.Album;

import java.util.List;

public interface AlbumListAsyncRespone {
    void processFinished(List<Album> albumList);

}
