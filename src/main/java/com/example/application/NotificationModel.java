package com.example.application;

public class NotificationModel {
    String from;
    String time;

    public NotificationModel(String from, String time) {
        this.from = from;
        this.time = time;
    }

    public String getFrom() {
        return from;
    }

    public String getTime() {
        return time;
    }
}
