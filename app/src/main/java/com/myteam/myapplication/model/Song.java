package com.myteam.myapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.myteam.myapplication.util.ServerInfo;

import java.io.Serializable;
import java.util.ArrayList;

public class Song implements Serializable, Cloneable {
    private int id;
    private String name;
    private Genre genre;
    private Artist artist;
    private Artist artist2;
    private Artist artist3;
    private Album album;
    private String src;
    private String img;
    private ArrayList<Artist> artists = new ArrayList<>();
    private boolean isActive;
    private String uri;

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Song() {
    }

    public String getUri(){
        return uri;
    }

    public void setUri(String uri){
        this.uri = uri;
    }
    public boolean isActive() {
        return isActive;
    }

        public void setActive(boolean active) {
        isActive = active;
    }

    public Song(int id, String name, Genre genre, Artist artist, String src, String img) {
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.artist = artist;
        this.src = src;
        this.img = img;
        this.isActive = false;
    }

    protected Song(Parcel in) {
        id = in.readInt();
        name = in.readString();
        src = in.readString();
        img = in.readString();
    }

    public ArrayList<Artist> getArtists() {
        return artists;
    }

    public void setArtists(ArrayList<Artist> artists) {
        this.artists = artists;
    }

    public Artist getArtist2() {
        return artist2;
    }

    public void setArtist2(Artist artist2) {
        this.artist2 = artist2;
    }

    public Artist getArtist3() {
        return artist3;
    }

    public void setArtist3(Artist artist3) {
        this.artist3 = artist3;
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

    // Khởi tạo đường dẫn lấy file ảnh
    public String getUrlImage() {
        return ServerInfo.SERVER_BASE + "/" + ServerInfo.STORAGE_SONG_IMG+ "/" + this.img;
    }

    // Thêm nghệ sĩ vào bài hát
    public void addArtist(Artist artist) {
        artists.add(artist);
    }

    // Lấy url file nhạc
    public String getUrlSrc() {
        return ServerInfo.SERVER_BASE + "/" + ServerInfo.STORAGE_SONG_MP3 + "/" + this.src;
    }

    // Lấy tên nghệ sĩ
    public String getArtistsName() {
        String name = artists.get(0).getName();

        for ( int i = 1; i < artists.size(); i++) {
            name += ", " + artists.get(i).getName();
        }
        return name;
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", genre=" + genre +
                ", album=" + album +
                ", src='" + src + '\'' +
                ", img='" + img + '\'' +
                ", artists=" + artists +
                '}';
    }
}
