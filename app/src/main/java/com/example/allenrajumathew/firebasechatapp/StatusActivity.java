package com.example.allenrajumathew.firebasechatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {

    //Toolbar Set
    private Toolbar mToolBar;

    //Android Fields
    private TextInputLayout mStatus;
    private Button mSavebtn;

    //Progress Set
    private ProgressDialog mLoginProgress;

    //Firebase
    private FirebaseUser mCurrentUser;
    private DatabaseReference mStatusDatabase;
//    private DatabaseReference mUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        String status_value = getIntent().getStringExtra("status_value");

        //Toolbar
        mToolBar = (Toolbar) findViewById(R.id.status_app_bar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Account Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Firebase
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
//        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

        String current_uid = mCurrentUser.getUid();

        mStatusDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);

        mStatus = (TextInputLayout) findViewById(R.id.status_input);
        mSavebtn = (Button) findViewById(R.id.status_save_btn);

//        //Set User is Online
//        mUserRef.child("online").setValue(true);

        //Display Current Status
        mStatus.getEditText().setText(status_value);

        mSavebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Call Progress Dialog
                mLoginProgress = new ProgressDialog(StatusActivity.this);
                mLoginProgress.setTitle("Saving Changes");
                mLoginProgress.setMessage("Making changes, Please Wait!");
                mLoginProgress.setCanceledOnTouchOutside(false);
                mLoginProgress.show();

                String status = mStatus.getEditText().getText().toString();

                mStatusDatabase.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            mLoginProgress.dismiss();
                            finish();
                        }
                        else{
                            Toast.makeText(StatusActivity.this, "Error occured while saving status", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
            }
        });


    }
}
