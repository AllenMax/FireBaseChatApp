package com.example.allenrajumathew.firebasechatapp.Model;

/**
 * Created by Allen Raju Mathew on 9/8/2017.
 */

public class BookmarkOrgList {

    public String title;
    public String time;
    public String note;

    public BookmarkOrgList() {}

    public BookmarkOrgList(String title, String time, String note) {
        this.title = title;
        this.time = time;
        this.note = note;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
