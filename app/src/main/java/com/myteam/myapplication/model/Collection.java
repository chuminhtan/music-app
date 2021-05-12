package com.myteam.myapplication.model;

import java.util.ArrayList;

public class Collection {
    private Playlist playlist;
    private ArrayList<Song> songs;

    public Collection(){}

    public Collection(Playlist playlist, ArrayList<Song> songs) {
        this.playlist = playlist;
        this.songs = songs;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }
}
