package com.example.allenrajumathew.firebasechatapp.Model;

import android.content.Context;
import android.database.Cursor;

import com.example.allenrajumathew.firebasechatapp.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Allen Raju Mathew on 8/17/2017.
 */

public class IndividualData {

    //**To change remove recordingBookMarkData , not needed
    public static List<IndividualListItem> getListData(Context context){

        String s = "SELECT * FROM tbl_RecordList";

        Cursor cursor = MainActivity.internalDBConnection.selectData(s);

        ArrayList recordingAudioID = new ArrayList();
        ArrayList recordingAudioTitle = new ArrayList();
        ArrayList recordingAudioDateData = new ArrayList();
        ArrayList recordingAudioTimeData = new ArrayList();

        while (cursor.moveToNext()){
            recordingAudioID.add(cursor.getString(0));
            recordingAudioTitle.add(cursor.getString(1));
            recordingAudioDateData.add(cursor.getString(2));
            recordingAudioTimeData.add(cursor.getString(3));
        }

        List<IndividualListItem> data = new ArrayList<>();

        for (int i=0; i<recordingAudioID.size() ; i++){
            IndividualListItem item = new IndividualListItem();
            item.setRecordingID(recordingAudioID.get(i).toString());
            item.setRecordingTitle(recordingAudioTitle.get(i).toString());
            item.setRecordingDateSubTitle(recordingAudioDateData.get(i).toString());
            item.setRecordingTimeSubTitle(recordingAudioTimeData.get(i).toString());
            data.add(item);
        }
        return data;
    }
}