package com.example.puzzlegame.Info;

public class PaperInfo {
    private long _id;
    private String title;
    private String preface;

    public long get_id(){
        return _id;
    }

    public String getTitle(){
        return title;
    }

    public String getPreface(){
        return  preface;
    }

    public void set_id(long id){
        _id=id;
    }

    public void setPreface(String preface) {
        this.preface = preface;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
