package com.example.allenrajumathew.firebasechatapp;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.allenrajumathew.firebasechatapp.Adapter.MessageAdapter;
import com.example.allenrajumathew.firebasechatapp.Model.Messages;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private String mChatUser;
    private String mUserName;
    private String mUserImage;

    private Toolbar mToolbar;

    private TextView mTitleView;
    private TextView mLastSeenView;
    private CircleImageView mProfileImage;

    private DatabaseReference mRootRef;
    private FirebaseAuth mAuth;
    private String mCurrentUserId;

    //Set Fields
    private ImageButton mChatAddBtn;
    private ImageButton mChatSendBtn;
    private EditText mChatMessageET;

    private RecyclerView mChatMessagesList;
    private SwipeRefreshLayout mChatRefreshLayout;

    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager mLinearLayout;
    private MessageAdapter mAdapter;

    private static final int TOTAL_ITEMS_TO_LOAD = 50;
    private int mCurrentPage =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();

        mChatUser = getIntent().getStringExtra("user_id");
        mUserName = getIntent().getStringExtra("user_name");
        mUserImage =getIntent().getStringExtra("user_profile_pic");

        //Set Fields
        mChatAddBtn = (ImageButton) findViewById(R.id.chat_add_btn);
        mChatSendBtn = (ImageButton) findViewById(R.id.chat_send_btn);
        mChatMessageET = (EditText) findViewById(R.id.chat_message_view);

        //Set RecyclerView
        mAdapter = new MessageAdapter(messagesList);

        mLinearLayout = new LinearLayoutManager(this);
        mChatMessagesList = (RecyclerView) findViewById(R.id.chat_messages_list);
        mChatRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.chat_swipe_messages_layout);
        mChatMessagesList.setHasFixedSize(true);
        mChatMessagesList.setLayoutManager(mLinearLayout);

        mChatMessagesList.setAdapter(mAdapter);

        loadMessages();

        //Tools Set
        mToolbar = (Toolbar) findViewById(R.id.chat_app_bar);
        setSupportActionBar(mToolbar);

        ActionBar actionChatBar = getSupportActionBar(); // Need to use it after "setSupportActionBar(mToolbar);"

        actionChatBar.setDisplayHomeAsUpEnabled(true);
        actionChatBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view = inflater.inflate(R.layout.chat_coustom_bar, null);

        actionChatBar.setCustomView(action_bar_view);

        // ------------------------------Custom Action Bar Items ------------------------------------

        mTitleView = (TextView) findViewById(R.id.chat_bar_title);
        mLastSeenView = (TextView) findViewById(R.id.chat_bar_seen);
        mProfileImage = (CircleImageView) findViewById(R.id.chat_bar_image);

        mTitleView.setText(mUserName);

        Picasso.with(ChatActivity.this).load(mUserImage).placeholder(R.drawable.default_avatar).into(mProfileImage);

        mRootRef.child("Users").child(mChatUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String online = dataSnapshot.child("online").getValue().toString();
                //String image = dataSnapshot.child("image").getValue().toString();

                if(online.equals("true")){
                    mLastSeenView.setText("Online");
                }
                else{
                    GetTimeAgo lastSee = new GetTimeAgo();
                    long lastTime = Long.parseLong(online);
                    String lastSeenTime = lastSee.getTimeAgo(lastTime);
                    mLastSeenView.setText(lastSeenTime);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mRootRef.child("Chat").child(mCurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(!dataSnapshot.hasChild(mChatUser)){

                    Map chatAddMap = new HashMap();
                    chatAddMap.put("seen",false);
                    chatAddMap.put("timestamp", ServerValue.TIMESTAMP);

                    Map chatUserMap = new HashMap();
                    chatUserMap.put("Chat/"+mCurrentUserId+"/"+mChatUser,chatAddMap);
                    chatUserMap.put("Chat/"+mChatUser+"/"+mCurrentUserId,chatAddMap);
                    
                    mRootRef.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            
                                if(databaseError != null){
                                    Log.d("Allen", "onComplete: "+databaseError.getMessage().toString());
                                }
 
 
                        }
                    });
                }
                
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mChatSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendMessage();

            }
        });

        mChatRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCurrentPage++;

                messagesList.clear();

                loadMessages();

                mChatMessagesList.scrollToPosition(30);
            }
        });


    }

    private void loadMessages() {

        DatabaseReference messageRef = mRootRef.child("Messages").child(mCurrentUserId).child(mChatUser);

        //Query
        Query messageQuery = messageRef.limitToLast(mCurrentPage * TOTAL_ITEMS_TO_LOAD);

        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Messages message = dataSnapshot.getValue(Messages.class);

                messagesList.add(message);
                mAdapter.notifyDataSetChanged();

                mChatMessagesList.scrollToPosition(messagesList.size() -1);

                mChatRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void sendMessage() {

        String message =mChatMessageET.getText().toString();

        if(!TextUtils.isEmpty(message)){

            String current_user_ref = "Messages/"+mCurrentUserId+"/"+mChatUser;
            String chat_user_ref = "Messages/"+mChatUser+"/"+mCurrentUserId;

            DatabaseReference user_message_push = mRootRef.child("Messages").child(mCurrentUserId).child(mChatUser).push();

            String push_id = user_message_push.getKey();

            Map messageMap = new HashMap();
            messageMap.put("message",message);
            messageMap.put("seen",false);
            messageMap.put("type","text");
            messageMap.put("time",ServerValue.TIMESTAMP);
            messageMap.put("from",mCurrentUserId);

            Map messageUserMap = new HashMap();
            messageUserMap.put(current_user_ref+"/"+push_id, messageMap);
            messageUserMap.put(chat_user_ref+"/"+push_id, messageMap);

            mChatMessageET.setText("");

            mRootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                    if(databaseError != null){
                        Log.d("Allen", "onComplete: "+databaseError.getMessage().toString());

                    }
                }
            });
        }
    }
}
