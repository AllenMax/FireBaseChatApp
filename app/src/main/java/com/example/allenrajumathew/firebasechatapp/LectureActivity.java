package com.example.allenrajumathew.firebasechatapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.allenrajumathew.firebasechatapp.Model.ClassList;
import com.example.allenrajumathew.firebasechatapp.Model.LectureList;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class LectureActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private RecyclerView mLectureList;

    private DatabaseReference mLectureDatabase;
    private FirebaseAuth mAuth;

    private String mCurrent_user_id;

    private static String class_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture);

        final String class_id = getIntent().getStringExtra("class_id");
        class_img = getIntent().getStringExtra("class_img");

        //Toolbar Set

        mToolbar = (Toolbar) findViewById(R.id.lecture_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Lecture List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);//**Change to "true"

        //Activity Fields

        mLectureList = (RecyclerView) findViewById(R.id.lecture_rec_list);
        mAuth = FirebaseAuth.getInstance();

        mCurrent_user_id = mAuth.getCurrentUser().getUid();

        mLectureDatabase = FirebaseDatabase.getInstance().getReference().child("Enrolled Lectures").child(mCurrent_user_id).child(class_id);
        mLectureDatabase.keepSynced(true);

        mLectureList.setHasFixedSize(true);
        mLectureList.setLayoutManager(new LinearLayoutManager(this));//Possible error with context

        if (!class_img.equals("default")){

            final ImageView lectureImgView = (ImageView) findViewById(R.id.lecture_im);
            Picasso.with(LectureActivity.this).load(class_img).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.img).into(lectureImgView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(LectureActivity.this).load(class_img).placeholder(R.drawable.img).into(lectureImgView);
                }
            });
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<LectureList, LectureHolder> friendsRecyclerViewAdapter
                = new FirebaseRecyclerAdapter<LectureList, LectureHolder>(
                LectureList.class,
                R.layout.activity_lecture_item,
                LectureHolder.class,
                mLectureDatabase
        ) {

            @Override
            protected void populateViewHolder(final LectureHolder viewHolder, LectureList model, int position) {

                final String list_lecture_id = getRef(position).getKey();

                mLectureDatabase.child(list_lecture_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {

                        final String lectureTitle = dataSnapshot.child("name").getValue().toString();
                        final String lectureSubTitle = dataSnapshot.child("date").getValue().toString();
                        final String lectureFavourite = dataSnapshot.child("favourite").getValue().toString();
                        final String lectureType = dataSnapshot.child("type").getValue().toString();
                        final String lectureRecording = dataSnapshot.child("recording").getValue().toString();

                        viewHolder.setTitle(lectureTitle);
                        viewHolder.setSubTitle(lectureSubTitle);
                        viewHolder.setFavourite(lectureFavourite);

                        final ImageView fav = (ImageView) viewHolder.mView.findViewById(R.id.lecture_favorite_im);

                        fav.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (lectureFavourite.equals("true")){
                                    fav.setImageResource(R.mipmap.ic_star);
                                    mLectureDatabase.child(list_lecture_id).child("favourite").setValue("false");
                                    Toast.makeText(LectureActivity.this, "Lecure Removed from Favourites", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    fav.setImageResource(R.mipmap.ic_star_shaded);
                                    mLectureDatabase.child(list_lecture_id).child("favourite").setValue("true");
                                    Toast.makeText(LectureActivity.this, "Lecure Added to Favourites", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (lectureType.equals("audio")){
                                    Intent bookmarkIntent = new Intent(LectureActivity.this, BookmarkOrgActivity.class);
                                    bookmarkIntent.putExtra("lecture_id", list_lecture_id);
                                    bookmarkIntent.putExtra("lecture_recording", lectureRecording);
                                    startActivity(bookmarkIntent);
                                }
                                else{
                                    Intent bookmarkIntent = new Intent(LectureActivity.this, BookmarkOrgActivityVideo.class);
                                    bookmarkIntent.putExtra("lecture_id", list_lecture_id);
                                    bookmarkIntent.putExtra("lecture_recording", lectureRecording);
                                    startActivity(bookmarkIntent);
                                }

                            }
                        });

//                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                                Intent bookmarkIntent = new Intent(LectureActivity.this, BookmarkOrgActivity.class);
//                                bookmarkIntent.putExtra("lecture_id", list_lecture_id);
//                                bookmarkIntent.putExtra("lecture_recording", lectureRecording);
//                                startActivity(bookmarkIntent);
//                            }
//                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };

        mLectureList.setAdapter(friendsRecyclerViewAdapter);
    }

    public static class LectureHolder extends RecyclerView.ViewHolder {

        View mView;

        public LectureHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setTitle(String title) {
            TextView lectureTitleView = (TextView) mView.findViewById(R.id.lecture_title_lbl);
            lectureTitleView.setText(title);
        }

        public void setSubTitle(String sub_title){
            TextView lectureSubTitleView = (TextView) mView.findViewById(R.id.lecture_sub_title_lbl);
            lectureSubTitleView.setText(sub_title);
        }

        public void setFavourite(String favourite){

            ImageView lectureFavouriteView = (ImageView) mView.findViewById(R.id.lecture_favorite_im);

            if (favourite.equals("true")){
                lectureFavouriteView.setImageResource(R.mipmap.ic_star_shaded);
            }
            else{
                lectureFavouriteView.setImageResource(R.mipmap.ic_star);
            }
        }
    }
}
