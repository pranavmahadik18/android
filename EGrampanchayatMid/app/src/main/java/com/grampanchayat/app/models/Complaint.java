package com.grampanchayat.app.models;
public class Complaint {
    public int id; public String name, phone, issue, status, date;
    public Complaint(int id, String name, String phone, String issue, String status, String date) {
        this.id=id; this.name=name; this.phone=phone; this.issue=issue; this.status=status; this.date=date;
    }
}
