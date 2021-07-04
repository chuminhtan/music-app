package com.myteam.myapplication.data;

import com.myteam.myapplication.model.Artist;

import java.util.ArrayList;
import java.util.List;

public interface ArtistListAsyncRespone {
    void processFinished(ArrayList<Artist> artistList);
}
