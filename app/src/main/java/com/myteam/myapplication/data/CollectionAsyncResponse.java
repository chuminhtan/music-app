package com.myteam.myapplication.data;

import com.myteam.myapplication.model.Artist;
import com.myteam.myapplication.model.Collection;

import java.util.ArrayList;

public interface CollectionAsyncResponse {
    void processFinished(Collection collection, ArrayList<Artist> artists);
}
