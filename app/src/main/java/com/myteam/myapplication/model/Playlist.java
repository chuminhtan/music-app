package com.myteam.myapplication.model;

public class Playlist {
    private int id;
    private User user;
    private String name;

    public Playlist() {}

    public Playlist(int id, User user, String name) {
        this.id = id;
        this.user = user;
        this.name = name;
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
}
