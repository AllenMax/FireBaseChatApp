package com.example.allenrajumathew.firebasechatapp.Model;

/**
 * Created by Allen Raju Mathew on 8/17/2017.
 */

public class IndividualListItem {
    private String recordingID;
    private String recordingTitle;
    private String recordingDateSubTitle;
    private String recordingTimeSubTitle;

    public String getRecordingTimeSubTitle() {
        return recordingTimeSubTitle;
    }

    public void setRecordingTimeSubTitle(String recordingTimeSubTitle) {
        this.recordingTimeSubTitle = recordingTimeSubTitle;
    }

    public String getRecordingID() {
        return recordingID;
    }

    public void setRecordingID(String recordingID) {
        this.recordingID = recordingID;
    }

    public String getRecordingTitle() {
        return recordingTitle;
    }

    public void setRecordingTitle(String recordingTitle) {
        this.recordingTitle = recordingTitle;
    }

    public String getRecordingDateSubTitle() {
        return recordingDateSubTitle;
    }

    public void setRecordingDateSubTitle(String recordingDateSubTitle) {
        this.recordingDateSubTitle = recordingDateSubTitle;
    }
}
