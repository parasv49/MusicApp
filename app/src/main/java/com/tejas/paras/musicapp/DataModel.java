package com.tejas.paras.musicapp;

/**
 * Created by paras on 2/4/2017.
 */
public class DataModel {

    String name;
    String data;
    String album;
    String duration;


    public DataModel(String name1, String data1, String album1, String duration1) {
        this.name=name1;
        this.data=data1;
        this.album=album1;
        this.duration=duration1;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return data;
    }

    public String getVersion_number() {
        return album;
    }

    public String getDuration() {
        return duration;
    }
}