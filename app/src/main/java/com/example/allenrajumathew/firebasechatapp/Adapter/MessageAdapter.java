package com.example.allenrajumathew.firebasechatapp.Adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.allenrajumathew.firebasechatapp.Model.Messages;
import com.example.allenrajumathew.firebasechatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Allen Raju Mathew on 8/14/2017.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{

    private List<Messages> mMessageList;
    private DatabaseReference mUserDatabase;

    private FirebaseAuth mAuth;

    public MessageAdapter(List<Messages> mMessageList) {

        this.mMessageList = mMessageList;

    }

    public String current_user;

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mAuth = FirebaseAuth.getInstance();

        current_user = mAuth.getCurrentUser().getUid();

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_message_single_layout ,parent, false);

        return new MessageViewHolder(v);

    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView messageText;
//        public TextView timeText;
//        public CircleImageView profileImage;
//        public TextView displayName;

        public MessageViewHolder(View view) {
            super(view);

            messageText = (TextView) view.findViewById(R.id.message_text_layout);
//            timeText = (TextView) view.findViewById(R.id.message_upload_time);
//            profileImage = (CircleImageView) view.findViewById(R.id.message_profile_layout);
//            displayName = (TextView) view.findViewById(R.id.name_text_layout);

        }
    }

    @Override
    public void onBindViewHolder(final MessageViewHolder viewHolder, int i) {

        Messages c = mMessageList.get(i);

        //Set Message Text
        viewHolder.messageText.setText(c.getMessage());

//        //Set Message Upload Time
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(c.getTime());
//
//        int mHour = calendar.get(Calendar.HOUR_OF_DAY);
//        int mMin = calendar.get(Calendar.MINUTE);
//
//        viewHolder.timeText.setText(mHour+":"+mMin);

        //Set user Dialoge Box
        String from_user = c.getFrom();

        if (from_user.equals(current_user)){

            viewHolder.messageText.setBackgroundResource(R.drawable.message_sent_text_background);
            viewHolder.messageText.setTextColor(Color.parseColor("#673bb7"));
        }
        else{

            viewHolder.messageText.setBackgroundResource(R.drawable.message_text_background);
            viewHolder.messageText.setTextColor(Color.WHITE);

        }

//        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(from_user);

//        mUserDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                String name = dataSnapshot.child("name").getValue().toString();
//                String image = dataSnapshot.child("thumb_image").getValue().toString();
//
////                viewHolder.displayName.setText(name);
//
////                Picasso.with(viewHolder.profileImage.getContext()).load(image)
////                        .placeholder(R.drawable.default_avatar).into(viewHolder.profileImage);
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//
//        });

        viewHolder.messageText.setText(c.getMessage());

    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }



}