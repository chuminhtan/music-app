package com.myteam.myapplication.model;

import com.myteam.myapplication.util.ServerInfo;

import java.io.Serializable;

public class Artist implements Serializable {
    private int id;
    private String name;
    private String story;
    private String img;

    public Artist() {

    }

    public Artist(int id, String name, String story, String img) {
        this.id = id;
        this.name = name;
        this.story = story;
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

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrlImg(){
        return ServerInfo.SERVER_BASE + "/storage/artist-image/" + img;
    }
}
