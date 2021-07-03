package com.myteam.myapplication.model;

public class LikeSong {
    private int songId;
    private int userId;

    public LikeSong(int songId, int userId) {
        this.songId = songId;
        this.userId = userId;
    }

    public LikeSong(){

    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
