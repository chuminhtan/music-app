package com.myteam.myapplication.data;

import com.myteam.myapplication.model.Song;

import java.util.ArrayList;

public interface SongAsyncResponeByID {
    void processFinished(Song song);
}
