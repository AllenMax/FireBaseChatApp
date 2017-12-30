package com.example.allenrajumathew.firebasechatapp.Model;

/**
 * Created by Allen Raju Mathew on 9/8/2017.
 */

public class LectureList {

    public String name, recording;

    public LectureList() {}

    public LectureList(String name, String recording) {
        this.name = name;
        this.recording = recording;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRecording() {
        return recording;
    }

    public void setRecording(String recording) {
        this.recording = recording;
    }
}
