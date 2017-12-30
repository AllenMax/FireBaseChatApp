package com.example.allenrajumathew.firebasechatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RegisterOrgActivity extends AppCompatActivity {

    private TextInputLayout mDisplayName;
    private Button mCreateBtn;

    private Toolbar mToolbar;

    private DatabaseReference mOrganizationsDatabase;
    private DatabaseReference mEnrolledDatabase;
    private DatabaseReference mEnrolledClassesDatabase;
    private DatabaseReference mEnrolledLecturesDatabase;
    private DatabaseReference mEnrolledBookmarksDatabase;

    //ProgressDialog
    private ProgressDialog mRegOrgProgress;

    //Current User
    //private FirebaseUser mCurrentUser;

    //Firebase Auth
    private FirebaseAuth mAuth;
    private String mCurrent_user_id;

    private static final int GALLERY_PICK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_org);

        //Toolbar Set

        mToolbar = (Toolbar) findViewById(R.id.register_org_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create Organization");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Progress Bar

        mRegOrgProgress = new ProgressDialog(this);

        // Firebase Auth

        mCurrent_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        mAuth = FirebaseAuth.getInstance();
//        mCurrent_user_id = mAuth.getCurrentUser().getUid();

        // Android Fields

        mDisplayName = (TextInputLayout) findViewById(R.id.reg_org_diaplay_name);
        mCreateBtn = (Button) findViewById(R.id.reg_org_create_btn);

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent galleryIntetnt = new Intent();
//                galleryIntetnt.setType("image/*");
//                galleryIntetnt.setAction(Intent.ACTION_GET_CONTENT);
//
//                startActivityForResult(Intent.createChooser(galleryIntetnt, "SELECT IMAGE"), GALLERY_PICK);


                String display_name = mDisplayName.getEditText().getText().toString();

                if (!TextUtils.isEmpty(display_name)) {

                    mRegOrgProgress.setTitle("Registering User");
                    mRegOrgProgress.setMessage("Please wait while we create your account !");
                    mRegOrgProgress.setCanceledOnTouchOutside(false);
                    mRegOrgProgress.show();

                    register_org(display_name);
                }
            }
        });

    }

    private void register_org(final String display_name) {

//        final String currentDate = DateFormat.getDateTimeInstance().format(new Date());
//
//        Map friendsMap = new HashMap();
//        friendsMap.put("Organizations/" + mCurrentUser.getUid() + "/" +  + "/date", currentDate);
//        friendsMap.put("Organizations/" + user_id + "/"  + mCurrentUser.getUid() + "/date", currentDate);
//
//
//        friendsMap.put("Friend_req/" + mCurrentUser.getUid() + "/" + user_id, null);
//        friendsMap.put("Friend_req/" + user_id + "/" + mCurrentUser.getUid(), null);
//
//
//        mRootRef.updateChildren(friendsMap, new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//
//                if(databaseError == null){
//
//                    mProfileSendReqBtn.setEnabled(true);
//                    mCurrent_state = "friends";
//                    mProfileSendReqBtn.setText("Unfriend this Person");
//
//                    mProfileDeclineBtn.setVisibility(View.INVISIBLE);
//                    mProfileDeclineBtn.setEnabled(false);
//
//                } else {
//
//                    String error = databaseError.getMessage();
//
//                    Toast.makeText(RegisterOrgActivity.this, error, Toast.LENGTH_SHORT).show();
//
//                }
//            }
//        });

        final String currentDate = DateFormat.getDateTimeInstance().format(new Date());

        HashMap<String, String> orgMap = new HashMap<>();
        orgMap.put("name", display_name);
        orgMap.put("img", " ");
        orgMap.put("thumb_image", " ");

        final HashMap<String, String> enrollMap = new HashMap<>();
        enrollMap.put("date", currentDate);

        final HashMap<String, String> classMap = new HashMap<>();
        classMap.put("name", "default class");
        classMap.put("sub title", "default sub title");
        classMap.put("img", " ");

        final HashMap<String, String> lectureMap = new HashMap<>();
        lectureMap.put("name", "default lecture");
        lectureMap.put("recording", " ");
        lectureMap.put("type", " ");
        lectureMap.put("date", currentDate);
        lectureMap.put("duration", " ");
        lectureMap.put("favourite", " ");

        final HashMap<String, String> bookmarkMap = new HashMap<>();
        bookmarkMap.put("title", "default bookmark title");
        bookmarkMap.put("time", "default time");
        bookmarkMap.put("note", " ");
        bookmarkMap.put("favourite", " ");
        bookmarkMap.put("lable", " ");

        //Path
        mOrganizationsDatabase = FirebaseDatabase.getInstance().getReference().child("Organizations").push();
        //Setting value to Database
        mOrganizationsDatabase.setValue(orgMap);
        //
        //Path
        mEnrolledDatabase = FirebaseDatabase.getInstance().getReference().child("Enrolled").child(mCurrent_user_id).child(mOrganizationsDatabase.getKey());
        //Setting value to Database
        mEnrolledDatabase.setValue(enrollMap);

        //*********************************************************************
        //
        //**To Change get key from enorolled
        //Path
        mEnrolledClassesDatabase = FirebaseDatabase.getInstance().getReference().child("Enrolled Classes").child(mCurrent_user_id).child(mOrganizationsDatabase.getKey()).push();
        //Setting value to Database
        mEnrolledClassesDatabase.setValue(classMap);
        //
        //Path
        mEnrolledLecturesDatabase = FirebaseDatabase.getInstance().getReference().child("Enrolled Lectures").child(mCurrent_user_id).child(mEnrolledClassesDatabase.getKey()).push();
        //Setting value to Database
        mEnrolledLecturesDatabase.setValue(lectureMap);
        //
        //Path
        mEnrolledBookmarksDatabase = FirebaseDatabase.getInstance().getReference().child("Enrolled Bookmarks").child(mCurrent_user_id).child(mEnrolledLecturesDatabase.getKey()).push();
        //Setting value to Database
        mEnrolledBookmarksDatabase.setValue(bookmarkMap);
        //Path
        mEnrolledBookmarksDatabase = FirebaseDatabase.getInstance().getReference().child("Enrolled Bookmarks").child(mCurrent_user_id).child(mEnrolledLecturesDatabase.getKey()).push();
        //Setting value to Database
        mEnrolledBookmarksDatabase.setValue(bookmarkMap);
        //Path
        mEnrolledBookmarksDatabase = FirebaseDatabase.getInstance().getReference().child("Enrolled Bookmarks").child(mCurrent_user_id).child(mEnrolledLecturesDatabase.getKey()).push();
        //Setting value to Database
        mEnrolledBookmarksDatabase.setValue(bookmarkMap);
        //Path
        mEnrolledBookmarksDatabase = FirebaseDatabase.getInstance().getReference().child("Enrolled Bookmarks").child(mCurrent_user_id).child(mEnrolledLecturesDatabase.getKey()).push();
        //Setting value to Database
        mEnrolledBookmarksDatabase.setValue(bookmarkMap);

        //#####################################################################
        //
        //Path
        mEnrolledLecturesDatabase = FirebaseDatabase.getInstance().getReference().child("Enrolled Lectures").child(mCurrent_user_id).child(mEnrolledClassesDatabase.getKey()).push();
        //Setting value to Database
        mEnrolledLecturesDatabase.setValue(lectureMap);
        //
        //Path
        mEnrolledBookmarksDatabase = FirebaseDatabase.getInstance().getReference().child("Enrolled Bookmarks").child(mCurrent_user_id).child(mEnrolledLecturesDatabase.getKey()).push();
        //Setting value to Database
        mEnrolledBookmarksDatabase.setValue(bookmarkMap);
        //Path
        mEnrolledBookmarksDatabase = FirebaseDatabase.getInstance().getReference().child("Enrolled Bookmarks").child(mCurrent_user_id).child(mEnrolledLecturesDatabase.getKey()).push();
        //Setting value to Database
        mEnrolledBookmarksDatabase.setValue(bookmarkMap);
        //Path
        mEnrolledBookmarksDatabase = FirebaseDatabase.getInstance().getReference().child("Enrolled Bookmarks").child(mCurrent_user_id).child(mEnrolledLecturesDatabase.getKey()).push();
        //Setting value to Database
        mEnrolledBookmarksDatabase.setValue(bookmarkMap);
        //Path
        mEnrolledBookmarksDatabase = FirebaseDatabase.getInstance().getReference().child("Enrolled Bookmarks").child(mCurrent_user_id).child(mEnrolledLecturesDatabase.getKey()).push();
        //Setting value to Database
        mEnrolledBookmarksDatabase.setValue(bookmarkMap);

        //*********************************************************************

        //Next Class
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //**To Change get key from enorolled
        //Path
        mEnrolledClassesDatabase = FirebaseDatabase.getInstance().getReference().child("Enrolled Classes").child(mCurrent_user_id).child(mOrganizationsDatabase.getKey()).push();
        //Setting value to Database
        mEnrolledClassesDatabase.setValue(classMap);
        //
        //Path
        mEnrolledLecturesDatabase = FirebaseDatabase.getInstance().getReference().child("Enrolled Lectures").child(mCurrent_user_id).child(mEnrolledClassesDatabase.getKey()).push();
        //Setting value to Database
        mEnrolledLecturesDatabase.setValue(lectureMap);
        //
        //Path
        mEnrolledBookmarksDatabase = FirebaseDatabase.getInstance().getReference().child("Enrolled Bookmarks").child(mCurrent_user_id).child(mEnrolledLecturesDatabase.getKey()).push();
        //Setting value to Database
        mEnrolledBookmarksDatabase.setValue(bookmarkMap);
        //Path
        mEnrolledBookmarksDatabase = FirebaseDatabase.getInstance().getReference().child("Enrolled Bookmarks").child(mCurrent_user_id).child(mEnrolledLecturesDatabase.getKey()).push();
        //Setting value to Database
        mEnrolledBookmarksDatabase.setValue(bookmarkMap);
        //Path
        mEnrolledBookmarksDatabase = FirebaseDatabase.getInstance().getReference().child("Enrolled Bookmarks").child(mCurrent_user_id).child(mEnrolledLecturesDatabase.getKey()).push();
        //Setting value to Database
        mEnrolledBookmarksDatabase.setValue(bookmarkMap);
        //Path
        mEnrolledBookmarksDatabase = FirebaseDatabase.getInstance().getReference().child("Enrolled Bookmarks").child(mCurrent_user_id).child(mEnrolledLecturesDatabase.getKey()).push();
        //Setting value to Database
        mEnrolledBookmarksDatabase.setValue(bookmarkMap);

        //#####################################################################

        //
        //Path
        mEnrolledLecturesDatabase = FirebaseDatabase.getInstance().getReference().child("Enrolled Lectures").child(mCurrent_user_id).child(mEnrolledClassesDatabase.getKey()).push();
        //Setting value to Database
        mEnrolledLecturesDatabase.setValue(lectureMap);
        //
        //Path
        mEnrolledBookmarksDatabase = FirebaseDatabase.getInstance().getReference().child("Enrolled Bookmarks").child(mCurrent_user_id).child(mEnrolledLecturesDatabase.getKey()).push();
        //Setting value to Database
        mEnrolledBookmarksDatabase.setValue(bookmarkMap);
        //Path
        mEnrolledBookmarksDatabase = FirebaseDatabase.getInstance().getReference().child("Enrolled Bookmarks").child(mCurrent_user_id).child(mEnrolledLecturesDatabase.getKey()).push();
        //Setting value to Database
        mEnrolledBookmarksDatabase.setValue(bookmarkMap);
        //Path
        mEnrolledBookmarksDatabase = FirebaseDatabase.getInstance().getReference().child("Enrolled Bookmarks").child(mCurrent_user_id).child(mEnrolledLecturesDatabase.getKey()).push();
        //Setting value to Database
        mEnrolledBookmarksDatabase.setValue(bookmarkMap);
        //Path
        mEnrolledBookmarksDatabase = FirebaseDatabase.getInstance().getReference().child("Enrolled Bookmarks").child(mCurrent_user_id).child(mEnrolledLecturesDatabase.getKey()).push();
        //Setting value to Database
        mEnrolledBookmarksDatabase.setValue(bookmarkMap);

        //*********************************************************************



        mRegOrgProgress.dismiss();

        Intent mainIntent = new Intent(RegisterOrgActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();


//        //Setting value to Database
//        mOrganizationsDatabase.setValue(orgMap).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//
//                mEnrolledDatabase = FirebaseDatabase.getInstance().getReference().child("Enrolled").child(mCurrent_user_id).child(mOrganizationsDatabase.getKey());
//
//                //Setting value to Database
//                mEnrolledDatabase.setValue(enrollMap).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//
//                        mEnrolledClassesDatabase = FirebaseDatabase.getInstance().getReference().child("Enrolled Classes").child(mCurrent_user_id).child(mOrganizationsDatabase.getKey());
//
//                        //Setting value to Database
//                        mEnrolledClassesDatabase.push().setValue(classMap);
//                        mEnrolledClassesDatabase.push().setValue(classMap).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//
//                                mEnrolledLecturesDatabase = FirebaseDatabase.getInstance().getReference().child("Enrolled Lectures").child(mCurrent_user_id).child(mEnrolledClassesDatabase.getKey());
//
//                                //Setting value to Database
//                                mEnrolledLecturesDatabase.push().setValue(lectureMap);
//                                mEnrolledLecturesDatabase.push().setValue(lectureMap).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//
//                                        mRegOrgProgress.dismiss();
//
//                                        Intent mainIntent = new Intent(RegisterOrgActivity.this, MainActivity.class);
//                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                        startActivity(mainIntent);
//                                        finish();
//                                    }
//                                });
//                            }
//                        });
//                    }
//                });
//            }
//        });

//        for (int i=0; i<=2; i++){
//
//            //Setting value to Database
//            mEnrolledClassesDatabase.setValue(classMap).addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void aVoid) {
//
//                    for (int i=0; i<=2; i++){
//
//                        //Setting value to Database
//                        mEnrolledLecturesDatabase.setValue(lectureMap).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//
//                                mRegOrgProgress.dismiss();
//
//                                Intent mainIntent = new Intent(RegisterOrgActivity.this, MainActivity.class);
//                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(mainIntent);
//                                finish();
//                            }
//                        });
//                    }
//                }
//            });
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
