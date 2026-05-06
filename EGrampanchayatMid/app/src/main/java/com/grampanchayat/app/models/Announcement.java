package com.grampanchayat.app.models;
public class Announcement {
    public int id; public String title, content, date;
    public Announcement(int id, String title, String content, String date) {
        this.id=id; this.title=title; this.content=content; this.date=date;
    }
}
