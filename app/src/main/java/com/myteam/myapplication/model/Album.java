package com.myteam.myapplication.model;

import com.myteam.myapplication.util.ServerInfo;

import java.io.Serializable;

public class Album implements Serializable {
    private int id;
    private Artist artist;
    private String name;
    private String img;

    public Album(int in, Artist artist, String name, String img) {
        this.id = in;
        this.artist = artist;
        this.name = name;
        this.img = img;
    }

    public Album() {
    }

    public int getId() {
        return id;
    }

    public void setId(int in) {
        this.id = in;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImageUrl() {

        return ServerInfo.SERVER_BASE + "/" + ServerInfo.ALBUM_IMG + "/" +this.img;
    }
}
