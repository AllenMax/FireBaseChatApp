package com.example.allenrajumathew.firebasechatapp.Internal_DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Allen Raju Mathew on 8/17/2017.
 */

public class DBConnection extends SQLiteOpenHelper {
    SQLiteDatabase sqldb;

    public DBConnection(Context context)
    {

        super(context,"Recorder",null,1);
    }

    public void openConnection()
    {

        sqldb=getWritableDatabase();
    }

    public void closeConnection()
    {

        sqldb.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String sql1="create table tbl_RecordList(" +
                "RecordID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "RecordName VARCHAR(200) NOT NULL, " +
                "RecordDate VARCHAR(200) NOT NULL, " +
                "RecordDuration VARCHAR(200) NOT NULL) ";

        String sql2="create table tbl_Bookmark(" +
                "BookmarkID INTEGER PRIMARY KEY NOT NULL, " +
                "RecordID INTEGER NOT NULL, " +
                "StampTime VARCHAR(200) NOT NULL,"+
                "Notes VARCHAR(200) NOT NULL)";
        sqLiteDatabase.execSQL(sql1);
        sqLiteDatabase.execSQL(sql2);

    }

    public boolean insertData(String qury)
    {
        try
        {
            sqldb.execSQL(qury);
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    public Cursor selectData(String qury)
    {
        try{
            Cursor cr=sqldb.rawQuery(qury,null);
            return  cr;
        }

        catch(Exception e){
            return null;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
