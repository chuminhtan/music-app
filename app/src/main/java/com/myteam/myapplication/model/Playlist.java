package com.myteam.myapplication.model;

import com.myteam.myapplication.util.ServerInfo;

public class Playlist {
    private int id;
    private User user;
    private String name;
    private String des;
    private String img;

    public Playlist() {}

    public Playlist(int id, User user, String name) {
        this.id = id;
        this.user = user;
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    // URL lấy file từ server
    public String getImageUrl() {

        return ServerInfo.SERVER_BASE + "/" + ServerInfo.PLAYLIST_IMG + "/" +this.img;
    }
}
