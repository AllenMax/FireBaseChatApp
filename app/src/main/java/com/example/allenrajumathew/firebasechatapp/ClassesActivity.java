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

import com.example.allenrajumathew.firebasechatapp.Model.ClassList;
import com.example.allenrajumathew.firebasechatapp.Model.OrganizationList;
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


public class ClassesActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private RecyclerView mClassList;

    private DatabaseReference mEnrolledDatabase;
    private DatabaseReference mClassesDatabase;
    private FirebaseAuth mAuth;

    private String mCurrent_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);

        //**To Change make it shared prefrence.
        final String org_id = getIntent().getStringExtra("org_id");

        //Toolbar Set

        mToolbar = (Toolbar) findViewById(R.id.classes_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Classes List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Activity Fields

        mClassList = (RecyclerView) findViewById(R.id.classes_rec_list);
        mAuth = FirebaseAuth.getInstance();

        mCurrent_user_id = mAuth.getCurrentUser().getUid();

        mEnrolledDatabase = FirebaseDatabase.getInstance().getReference().child("Enrolled").child(mCurrent_user_id).child(org_id);
        mClassesDatabase = FirebaseDatabase.getInstance().getReference().child("Enrolled Classes").child(mCurrent_user_id).child(org_id);
        mEnrolledDatabase.keepSynced(true);
        mClassesDatabase.keepSynced(true);

        mClassList.setHasFixedSize(true);
        mClassList.setLayoutManager(new LinearLayoutManager(this));//Possible error with context

    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerAdapter<ClassList, ClassHolder> friendsRecyclerViewAdapter
        = new FirebaseRecyclerAdapter<ClassList, ClassHolder> (
                ClassList.class,
                R.layout.activity_classes_item,
                ClassHolder.class,
                mClassesDatabase
        ){

            @Override
            protected void populateViewHolder(final ClassHolder viewHolder, ClassList model, int position) {

                final String list_class_id = getRef(position).getKey();

                mClassesDatabase.child(list_class_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final String classTitle = dataSnapshot.child("name").getValue().toString();
                        final String classSubTitle = dataSnapshot.child("sub title").getValue().toString();
                        final String classImg = dataSnapshot.child("img").getValue().toString();

                        viewHolder.setTitle(classTitle);
                        viewHolder.setSubTitle(classSubTitle);
                        viewHolder.setImg(classImg, ClassesActivity.this);//Possible error with context

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent lectureIntent = new Intent(ClassesActivity.this, LectureActivity.class);
                                lectureIntent.putExtra("class_id", list_class_id);
                                lectureIntent.putExtra("class_img", classImg);
                                startActivity(lectureIntent);
                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };

        mClassList.setAdapter(friendsRecyclerViewAdapter);

    }

    public static class ClassHolder extends RecyclerView.ViewHolder {

        View mView;

        public ClassHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setTitle(String title){
            TextView classTitleView = (TextView) mView.findViewById(R.id.class_title_lbl);
            classTitleView.setText(title);
        }

        public void setSubTitle(String sub_title){
            TextView classSubTitleView = (TextView) mView.findViewById(R.id.class_sub_title_lbl);
            classSubTitleView.setText(sub_title);
        }

        public void setImg (final String image, final Context context){

            if (!image.equals("default")){

                final ImageView classImgView = (ImageView) mView.findViewById(R.id.class_im);
                Picasso.with(context).load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.img).into(classImgView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(context).load(image).placeholder(R.drawable.img).into(classImgView);
                    }
                });
            }
        }
    }
}
