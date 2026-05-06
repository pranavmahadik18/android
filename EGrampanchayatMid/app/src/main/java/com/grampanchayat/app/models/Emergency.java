package com.grampanchayat.app.models;
public class Emergency {
    public int id; public String title, message, alertType, date;
    public Emergency(int id, String title, String message, String alertType, String date) {
        this.id=id; this.title=title; this.message=message; this.alertType=alertType; this.date=date;
    }
}
