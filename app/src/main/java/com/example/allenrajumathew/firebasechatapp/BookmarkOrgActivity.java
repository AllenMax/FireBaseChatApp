package com.example.allenrajumathew.firebasechatapp;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.allenrajumathew.firebasechatapp.Model.BookmarkOrgList;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

/**
 * Created by Allen Raju Mathew on 9/8/2017.
 */

public class BookmarkOrgActivity extends AppCompatActivity implements Runnable{

    private Toolbar mToolbar;

    private RecyclerView mBookmarkList;

    private DatabaseReference mBookmarkDatabase;
    private FirebaseAuth mAuth;

    private String mCurrent_user_id;

    //Android Fields
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
    String lecture_recording="";

    //////////////////////////////////////////////////////////////////

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        final String lecture_id = getIntent().getStringExtra("lecture_id");
        lecture_recording = getIntent().getStringExtra("lecture_recording");

        //Toolbar Set

        mToolbar = (Toolbar) findViewById(R.id.bookmark_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Bookmark List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);//**Change to "true"

        //Activity Fields

        mBookmarkList = (RecyclerView) findViewById(R.id.rec_bookmark_list);
        mAuth = FirebaseAuth.getInstance();

        mCurrent_user_id = mAuth.getCurrentUser().getUid();

        mBookmarkDatabase = FirebaseDatabase.getInstance().getReference().child("Enrolled Bookmarks").child(mCurrent_user_id).child(lecture_id);
        mBookmarkDatabase.keepSynced(true);

        mBookmarkList.setHasFixedSize(true);
        mBookmarkList.setLayoutManager(new LinearLayoutManager(this));//Possible error with context

        startTime = (TextView) findViewById(R.id.startTimerTextView);
        endTime = (TextView) findViewById(R.id.endTimeTextView);

        startButton = (ImageView) findViewById(R.id.im_mp_play);
        soundSeekBar = (SeekBar) findViewById(R.id.soundseekBar);

        prv5 = (ImageView) findViewById(R.id.im_mp_replay5);
        fow5 = (ImageView) findViewById(R.id.im_mp_forward5);

        //////////////////////////////////////////////////////////////////

        prv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int subTime = progressTime - 5000;

                if (subTime >= progressTime){
                    soundPlayer.stop();
                    soundPlayer = MediaPlayer.create(getBaseContext(), Uri.parse(lecture_recording));
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
                    soundPlayer = MediaPlayer.create(getBaseContext(), Uri.parse(lecture_recording));
                }
                else{
                    soundPlayer.seekTo(addTime);
                }

            }
        });

        soundPlayer = MediaPlayer.create(this.getBaseContext(), Uri.parse(lecture_recording));

//        soundPlayer = new MediaPlayer();
//        soundPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

//        fetchAudioUrlFromFirebase();

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

        soundThread = new Thread(this);//Possible error
        soundThread.start();
    }

//    private void fetchAudioUrlFromFirebase() {
//        final FirebaseStorage storage = FirebaseStorage.getInstance();
//        // Create a storage reference from our app
//        StorageReference storageRef = storage.getReferenceFromUrl(lecture_recording);
//        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                try {
//                    // Download url of file
//                    final String url = uri.toString();
//                    soundPlayer.setDataSource(url);
//                    // wait for media player to get prepare
////                    soundPlayer.setOnPreparedListener(BookmarkOrgActivity.this);
////                    soundPlayer.prepareAsync();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.i("TAG", e.getMessage());
//                    }
//                });
//
//    }

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
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<BookmarkOrgList, BookmarkHolder> friendsRecyclerViewAdapter
                = new FirebaseRecyclerAdapter<BookmarkOrgList, BookmarkHolder>(
                BookmarkOrgList.class,
                R.layout.activity_bookmark_list,
                BookmarkHolder.class,
                mBookmarkDatabase
        ){


            @Override
            protected void populateViewHolder(final BookmarkHolder viewHolder, BookmarkOrgList model, int position) {

                final String list_bookmark_id = getRef(position).getKey();

                mBookmarkDatabase.child(list_bookmark_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final String bookmarkTitle = dataSnapshot.child("title").getValue().toString();
                        final String bookmarkTime = dataSnapshot.child("time").getValue().toString();
                        final String bookmarkNote = dataSnapshot.child("note").getValue().toString();
                        //final String bookmarkFavourite = dataSnapshot.child("favourite").getValue().toString();

                        viewHolder.setName(bookmarkTitle);
                        viewHolder.setTime(bookmarkTime);
                        viewHolder.setNote(bookmarkNote);

//                        final ImageView fav = (ImageView) viewHolder.mView.findViewById(R.id.bookmark_favorite_im);
//
//                        fav.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                                if (lectureFavourite.equals("true")){
//                                    fav.setImageResource(R.mipmap.ic_star);
//                                    mBookmarkDatabase.child(list_bookmark_id).child("favourite").setValue("false");
//                                    Toast.makeText(BookmarkOrgActivity.this, "BookMark Removed from Favourites", Toast.LENGTH_SHORT).show();
//                                }
//                                else{
//                                    fav.setImageResource(R.mipmap.ic_star_shaded);
//                                    mBookmarkDatabase.child(list_bookmark_id).child("favourite").setValue("true");
//                                    Toast.makeText(BookmarkOrgActivity.this, "BookMark Added to Favourites", Toast.LENGTH_SHORT).show();
//                                }
//
//                            }
//                        });

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                soundPlayer.seekTo(Integer.parseInt(bookmarkTime));
                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };

        mBookmarkList.setAdapter(friendsRecyclerViewAdapter);
    }



    public static class BookmarkHolder extends RecyclerView.ViewHolder {

        View mView;

        public BookmarkHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setName(String name) {
            TextView bookmarkTitleView = (TextView) mView.findViewById(R.id.bookmark_title_lbl);
            bookmarkTitleView.setText(name);
        }

        public void setTime(String time){
            TextView timeSubTitleView = (TextView) mView.findViewById(R.id.bookmark_sub_title_lbl);
            String formatedTimer = getFormat(Long.parseLong(time));

            timeSubTitleView.setText(formatedTimer);
        }

        private String getFormat(long millis) {
            return String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millis),
                    TimeUnit.MILLISECONDS.toSeconds(millis) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
            );
        }

        public void setNote(String note){
            TextView noteSubTitleView = (TextView) mView.findViewById(R.id.bookmark_note_lbl);
            noteSubTitleView.setText(note);
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
