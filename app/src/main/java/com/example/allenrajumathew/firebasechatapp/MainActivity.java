package com.example.allenrajumathew.firebasechatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.allenrajumathew.firebasechatapp.Internal_DataBase.DBConnection;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Toolbar mToolbar;

    //Initializing Tabs Or View Pages
    private ViewPager mViewPager;

    //Initializing View Adapter
    private SectionsPagerAdapter mSectionsPagerAdapter;

    //Initializing User Ref
    private DatabaseReference mUserRef;

    private TabLayout mTabLayout;

    //Users using the app
    private FirebaseUser mCurrentUser;

    //Internal Data Base
    public static DBConnection internalDBConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        //DataBase Connection
        //Is called from this Activity and accessed from everywhere
        internalDBConnection = new DBConnection(this);
        internalDBConnection.openConnection();

        //Tools Set
        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("BookMark");

        //User Ref
        mCurrentUser= mAuth.getCurrentUser();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

//        User Ref
//        if(mAuth.getCurrentUser() !=null){
//            mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
//        }

        //Tabs Set
        mViewPager = (ViewPager) findViewById(R.id.main_tabPager);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1);

        mTabLayout = (TabLayout) findViewById(R.id.main_tabs);
        mTabLayout.setupWithViewPager(mViewPager);


    }

    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.

        if (mCurrentUser == null) {

            sendToStratActivity();

        } else {

            mUserRef.child("online").setValue("true");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mCurrentUser != null) {

            mUserRef.child("online").setValue(ServerValue.TIMESTAMP);//Time Stap of Server
        }
    }

    private void sendToStratActivity() {

        Intent startIntent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(startIntent);

        //Prevent users from accessing this Activity
        //By Clicking on the Back Button
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.main_logout_btn) {

            //Sign out User
            FirebaseAuth.getInstance().signOut();
            sendToStratActivity();
        }

        if (item.getItemId() == R.id.main_settings_btn) {

            //Display User Profile
            Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
        }

        if (item.getItemId() == R.id.main_all_btn) {

            //Display User Profile
            Intent allUsersIntent = new Intent(MainActivity.this, AllUsersActivity.class);
            startActivity(allUsersIntent);
        }


        return true;
    }
}
