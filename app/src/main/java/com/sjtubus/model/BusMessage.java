package com.sjtubus.model;

import java.sql.Time;
import java.util.Date;

import okhttp3.internal.http.HttpDate;

public class BusMessage {
    private String title;
    private String content;
    private String sender;
    private Date time;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
