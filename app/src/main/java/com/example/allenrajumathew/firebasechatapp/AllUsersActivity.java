package com.example.allenrajumathew.firebasechatapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.allenrajumathew.firebasechatapp.Model.AllUsersList;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllUsersActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private RecyclerView mAllUsersList;

    private FirebaseUser mCurrentUser;
    private DatabaseReference mUsersDatabase;
//    private DatabaseReference mUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);


        mToolbar = (Toolbar) findViewById(R.id.all_users_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
//        mUserRef = mUsersDatabase.child(mCurrentUser.getUid());

        mAllUsersList = (RecyclerView) findViewById(R.id.all_users_rec_list);
        mAllUsersList.setLayoutManager(new LinearLayoutManager(this));
        mAllUsersList.setHasFixedSize(true);

    }

    //In order to retrive data in real time
    //We create a firebase recycler adapter
    //the adapter will be set to mAllUsersList
    @Override
    protected void onStart() {
        super.onStart();

        //Requires Model Class and View Holder
        FirebaseRecyclerAdapter<AllUsersList,AllUsersViewHolder > firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<AllUsersList, AllUsersViewHolder>(
                AllUsersList.class,
                R.layout.activity_all_users_item,
                AllUsersViewHolder.class,
                mUsersDatabase

        ) {
            @Override
            protected void populateViewHolder(AllUsersViewHolder viewHolder, AllUsersList model, int position) {

                viewHolder.setName(model.getName());
                viewHolder.setStatus(model.getStatus());
                viewHolder.setUserImage(model.getThumb_image(), getApplicationContext());

                final String user_id = getRef(position).getKey();

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent profileIntent = new Intent(AllUsersActivity.this, ProfileActivity.class);
                        profileIntent.putExtra("user_id",user_id);
                        startActivity(profileIntent);
                    }
                });
            }
        };

        mAllUsersList.setAdapter(firebaseRecyclerAdapter);
    }

    //Since it is an inner class we need to use static
    public static class AllUsersViewHolder extends RecyclerView.ViewHolder{

        //This is the complet relative layout for a single list item
        View mView;

        public AllUsersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setName(String name){

            TextView userNameView = (TextView) mView.findViewById(R.id.all_users_item_display_name);
            userNameView.setText(name);
        }

        public void setStatus(String status){

            TextView userStatusView = (TextView) mView.findViewById(R.id.all_users_item_status);
            userStatusView.setText(status);
        }

        public void setUserImage(String thumb_image, Context context){

            CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.all_users_item_image);
            Picasso.with(context).load(thumb_image).placeholder(R.drawable.default_avatar).into(userImageView);
        }
    }
}
