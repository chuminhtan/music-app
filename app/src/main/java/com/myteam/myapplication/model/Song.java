package com.myteam.myapplication.model;

public class Song {
    private int id;
    private String name;
    private Genre genre;
    private Artist artist;
    private Album album;
    private String src;
    private String img;

    public Song() {
    }

    public Song(int id, String name, Genre genre, Artist artist, String src, String img) {
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.artist = artist;
        this.src = src;
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
