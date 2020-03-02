package com.example.myapplication;

import java.io.Serializable;

public class Song implements Serializable {
    private String name;
    private String author;
    private String songPath;
    private int imageId;

    public Song(String name,String author,String songPath,int imageId){
        this.name = name;
        this.author = author;
        this.imageId = imageId;
        this.songPath = songPath;
    }

    public String getName(){
        return name;
    }

    public String getAuthor(){
        return author;
    }

    public int getImageId() {
        return imageId;
    }

    public String getSongPath(){return songPath;}
}
