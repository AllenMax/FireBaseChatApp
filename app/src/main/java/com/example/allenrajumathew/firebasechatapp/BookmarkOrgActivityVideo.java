package com.example.allenrajumathew.firebasechatapp;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

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
 * Created by Allen Raju Mathew on 9/11/2017.
 */

public class BookmarkOrgActivityVideo extends AppCompatActivity {

    private Toolbar mToolbar;

    private RecyclerView mBookmarkList;

    private DatabaseReference mBookmarkDatabase;
    private FirebaseAuth mAuth;

    private String mCurrent_user_id;

    VideoView videoView;
    MediaController mediaController;

    String lecture_recording="";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark_video);

        final String lecture_id = getIntent().getStringExtra("lecture_id");
        lecture_recording = getIntent().getStringExtra("lecture_recording");

        //Toolbar Set

        mToolbar = (Toolbar) findViewById(R.id.bookmark_video_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Bookmark List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        //Activity Fields

        mBookmarkList = (RecyclerView) findViewById(R.id.rec_bookmark_video_list);
        mAuth = FirebaseAuth.getInstance();

        mCurrent_user_id = mAuth.getCurrentUser().getUid();

        mBookmarkDatabase = FirebaseDatabase.getInstance().getReference().child("Enrolled Bookmarks").child(mCurrent_user_id).child(lecture_id);
        mBookmarkDatabase.keepSynced(true);

        mBookmarkList.setHasFixedSize(true);
        mBookmarkList.setLayoutManager(new LinearLayoutManager(this));//Possible error with context

        //Android Fields
        videoView = (VideoView) findViewById(R.id.bookmark_videoView);
        mediaController= new MediaController(BookmarkOrgActivityVideo.this);

        Uri uri=Uri.parse(lecture_recording);

        //Setting MediaController and URI, then starting the videoView
        videoView.setVideoURI(uri);
        videoView.setMediaController(mediaController);

        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                mediaController.setAnchorView(videoView);
                videoView.requestFocus();
                videoView.start();
                return false;
            }
        });

//        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                Log.i("Allen", "Duration = " +videoView.getDuration());
//                Toast.makeText(BookmarkOrgActivityVideo.this, "Prepared", Toast.LENGTH_SHORT).show();
//                videoView.start();
//            }
//        });

    }

//    public void prepaerVideo(){
//        Uri uri=Uri.parse(lecture_recording);
//
//        //Setting MediaController and URI, then starting the videoView
//        videoView.setVideoURI(uri);
//        videoView.setMediaController(mediaController);
//        mediaController.setAnchorView(videoView);
//        videoView.requestFocus();
//        videoView.start();
//    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<BookmarkOrgList, BookmarkOrgActivity.BookmarkHolder> friendsRecyclerViewAdapter
                = new FirebaseRecyclerAdapter<BookmarkOrgList, BookmarkOrgActivity.BookmarkHolder>(
                BookmarkOrgList.class,
                R.layout.activity_bookmark_list,
                BookmarkOrgActivity.BookmarkHolder.class,
                mBookmarkDatabase
        ){


            @Override
            protected void populateViewHolder(final BookmarkOrgActivity.BookmarkHolder viewHolder, BookmarkOrgList model, int position) {

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
                                videoView.seekTo(Integer.parseInt(bookmarkTime));
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


    }

    @Override
    protected void onPause() {
        super.onPause();

    }

}