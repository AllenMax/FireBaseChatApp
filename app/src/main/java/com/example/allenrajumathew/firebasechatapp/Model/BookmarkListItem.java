package com.example.allenrajumathew.firebasechatapp.Model;

/**
 * Created by Allen Raju Mathew on 8/17/2017.
 */

public class BookmarkListItem {

    private String bookmarkTitle;
    private String bookmarkTimeSubTitle;
    private String bookmarkNotes;

    private boolean bookmarkFavourite = false;

    public String getBookmarkTitle() {
        return bookmarkTitle;
    }

    public void setBookmarkTitle(String bookmarkTitle) {
        this.bookmarkTitle = bookmarkTitle;
    }

    public String getBookmarkTimeSubTitle() {
        return bookmarkTimeSubTitle;
    }

    public void setBookmarkTimeSubTitle(String bookmarkTimeSubTitle) {
        this.bookmarkTimeSubTitle = bookmarkTimeSubTitle;
    }

    public String getBookmarkNotes() {
        return bookmarkNotes;
    }

    public void setBookmarkNotes(String bookmarkNotes) {
        this.bookmarkNotes = bookmarkNotes;
    }
}
