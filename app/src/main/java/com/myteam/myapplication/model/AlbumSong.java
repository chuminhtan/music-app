package com.myteam.myapplication.model;

import java.util.ArrayList;

public class AlbumSong {
    private Album album;
    private ArrayList<Song> songs;

    public AlbumSong(){};

    public AlbumSong(Album album, ArrayList<Song> songs) {
        this.album = album;
        this.songs = songs;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }
}
