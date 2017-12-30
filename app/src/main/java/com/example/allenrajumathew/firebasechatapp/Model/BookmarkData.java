package com.example.allenrajumathew.firebasechatapp.Model;

import android.content.Context;
import android.database.Cursor;

import com.example.allenrajumathew.firebasechatapp.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Allen Raju Mathew on 8/17/2017.
 */

public class BookmarkData {

    public static List<BookmarkListItem> getBookmarkListData(Context context) {

        String s = "SELECT * FROM tbl_Bookmark";

        Cursor cursor = MainActivity.internalDBConnection.selectData(s);

        ArrayList recordingBookMarkData = new ArrayList();

        while (cursor.moveToNext()) {
            recordingBookMarkData.add(cursor.getString(2));
        }

        List<BookmarkListItem> data = new ArrayList<>();

        //Create ListItem with dummy data, then add them our List
        for (int i = 0; i < recordingBookMarkData.size(); i++) {
            BookmarkListItem item = new BookmarkListItem();
            item.setBookmarkTimeSubTitle(recordingBookMarkData.get(i).toString());
            data.add(item);
        }

        return data;

    }

}
