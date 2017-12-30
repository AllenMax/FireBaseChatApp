package com.example.allenrajumathew.firebasechatapp.UI;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.allenrajumathew.firebasechatapp.Adapter.BookmarkAdapter;
import com.example.allenrajumathew.firebasechatapp.MainActivity;
import com.example.allenrajumathew.firebasechatapp.Model.BookmarkListItem;
import com.example.allenrajumathew.firebasechatapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Allen Raju Mathew on 8/17/2017.
 */

public class BookmarkActivity extends AppCompatActivity implements Runnable{

    //////////////////////////////////////////////////////////////////
    TextView startTime;
    TextView endTime;

    public static MediaPlayer soundPlayer;
    private SeekBar soundSeekBar;
    private Thread soundThread;

    private int progressTime;

    private ImageView startButton;
//    private ImageView stopButton;

    private ImageView prv5;
    private ImageView fow5;

    private long startHTime = 0L;
    private Handler customHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    int secs;
    int mins;
    int hours;

    int flag=0;
    String s="";

    //////////////////////////////////////////////////////////////////

    private RecyclerView recBookmarkView;
    private BookmarkAdapter adapter;

    List<BookmarkListItem> data = new ArrayList<>();

    //////////////////////////////////////////////////////////////////

    ///////////////////////////
    private ArrayList listData;
    ///////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        //////////////////////////////////////////////////////////////////
        s = getIntent().getStringExtra("Audio");

        String id = getIntent().getStringExtra("ID");

        String qurey = "SELECT * FROM tbl_Bookmark WHERE RecordID ="+id;

        Cursor cursor = MainActivity.internalDBConnection.selectData(qurey);

        while (cursor.moveToNext()){
//        Toast.makeText(getBaseContext(), cursor.getString(2), Toast.LENGTH_SHORT).show();
//        Toast.makeText(getBaseContext(), cursor.getString(3), Toast.LENGTH_SHORT).show();
            BookmarkListItem item = new BookmarkListItem();
            item.setBookmarkTimeSubTitle(cursor.getString(2));
            item.setBookmarkNotes(cursor.getString(3));
            data.add(item);
        }

        recBookmarkView = (RecyclerView) findViewById(R.id.rec_bookmark_list);

        recBookmarkView.setLayoutManager(new LinearLayoutManager(this));
        recBookmarkView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        adapter = new BookmarkAdapter(data, this);
        recBookmarkView.setAdapter(adapter);

        //////////////////////////////////////////////////////////////////

        endTime = (TextView) findViewById(R.id.endTimeTextView);
        startTime = (TextView) findViewById(R.id.startTimerTextView);

        prv5 = (ImageView) findViewById(R.id.im_mp_replay5);
        fow5 = (ImageView) findViewById(R.id.im_mp_forward5);

        //////////////////////////////////////////////////////////////////

        prv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int subTime = progressTime - 5000;

                if (subTime >= progressTime){
                    soundPlayer.stop();
                    soundPlayer = MediaPlayer.create(getBaseContext(), Uri.parse(s));
                }
                else{
                    Log.d("Allen", "onClick: "+subTime);
                    soundPlayer.seekTo(subTime);
                }
            }
        });

        fow5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int addTime = progressTime + 5000;

                if (addTime >= soundPlayer.getDuration()){
                    soundPlayer.stop();
                    soundPlayer = MediaPlayer.create(getBaseContext(), Uri.parse(s));
//                    Toast.makeText(context, "End Of Recording", Toast.LENGTH_SHORT).show();
                }
                else{
                    soundPlayer.seekTo(addTime);
                }

            }
        });

        startButton = (ImageView) findViewById(R.id.im_mp_play);


        soundSeekBar = (SeekBar) findViewById(R.id.soundseekBar);
        soundPlayer = MediaPlayer.create(this.getBaseContext(), Uri.parse(s));

        Handler customHandler = new Handler();

        int duration=soundPlayer.getDuration();
        int hours=0;
        int secs=0;
        int mins=0;
        secs = (int) (duration / 1000);
        hours = mins/60;
        mins = secs / 60;
        secs = secs % 60;
        if (endTime != null)
            endTime.setText("" + String.format("%02d", mins) + ":"+ String.format("%02d", secs));


        setupListeners();

        soundThread = new Thread(this);
        soundThread.start();

    }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            if(flag==0)
            {
                updatedTime = timeSwapBuff + timeInMilliseconds;
            }
            else
            {
                updatedTime=updatedTime+1000;
            }
            if(updatedTime==soundPlayer.getDuration())
            {
                customHandler.removeCallbacks(updateTimerThread);
            }

            secs = (int) (updatedTime / 1000);

            mins = secs / 60;
            hours = mins / 60;
            secs = secs % 60;
            mins = mins% 60;

            Log.d("hai","hereeeeeeeeeeeee");
            Log.d("hai",Integer.toString(hours));
            Log.d("hai",Integer.toString(mins));
            Log.d("hai",Integer.toString(secs));
            if (startTime != null)
                startTime.setText("" + String.format("%02d", hours)+":"+String.format("%02d", mins)
                        + ":"+ String.format("%02d", secs)
                );
            customHandler.removeCallbacks(updateTimerThread);

        }

    };


    private void setupListeners() {

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (soundPlayer.isPlaying()) {
                    soundPlayer.pause();
                    startButton.setImageResource(R.mipmap.ic_play);  //Your playbutton image
                } else {
                    soundPlayer.start();
                    startButton.setImageResource(R.mipmap.ic_pause);  //Your pausebutton image
                }
                //customHandler.postDelayed(updateTimerThread, 0);

            }
        });

//        stopButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                soundPlayer.stop();
//                soundPlayer = MediaPlayer.create(getBaseContext(), Uri.parse(s));
//            }
//        });

        soundSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                progressTime =progress;

                if (fromUser) {

                    soundPlayer.seekTo(progress);
                    updatedTime=progress;
                    secs = (int) (progress / 1000);
                    mins = secs / 60;
                    secs = secs % 60;
                    if (startTime != null)
                        startTime.setText("" + String.format("%02d", mins) + ":"
                                + String.format("%02d", secs));
                }
                else
                {
                    currentTime (progress);

//                    Toast.makeText(context, Integer.toString(trial), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public void currentTime ( int progress){
        secs = (int) (progress / 1000);
        mins = secs / 60;
        secs = secs % 60;
        Log.d("testrun","calling"+secs);
        if (startTime != null)
            startTime.setText("" + String.format("%02d", mins) + ":"
                    + String.format("%02d", secs));
    }

    @Override
    public void run() {
        int currentPosition = 0;
        int soundTotal = soundPlayer.getDuration();

//        endTime.setText( Integer.toString (soundTotal));
        soundSeekBar.setMax(soundTotal);

        while (soundPlayer != null && currentPosition < soundTotal) {
            try {
                Thread.sleep(300);
                currentPosition = soundPlayer.getCurrentPosition();
            } catch (InterruptedException soundException) {
                return;
            } catch (Exception otherException) {
                return;
            }
            soundSeekBar.setProgress(currentPosition);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPlayer.stop();

    }

    @Override
    protected void onPause() {
        super.onPause();
        soundPlayer.stop();
    }
}