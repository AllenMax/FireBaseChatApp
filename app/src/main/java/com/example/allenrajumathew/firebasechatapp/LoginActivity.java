package com.example.allenrajumathew.firebasechatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout mLoginEmail;
    private TextInputLayout mLoginPassword;
    private Button mLoginBtn;

    private Toolbar mToolbar;

    //Progress Dialog
    private ProgressDialog mLoginProgress;

    private FirebaseAuth mAuth;

    private DatabaseReference mUserDatabase;

    //**Improvements**

    //Display warning if wifi is not connected

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Toolbar Set
        mToolbar = (Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//will Display arrow and when clicked will go back

        //Progress Set
        mLoginProgress = new ProgressDialog(this);

        //Database
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        //FireBase Authentication
        mAuth = FirebaseAuth.getInstance();

        //Android Fields
        mLoginEmail = (TextInputLayout) findViewById(R.id.login_email);
        mLoginPassword = (TextInputLayout) findViewById(R.id.login_password);
        mLoginBtn = (Button) findViewById(R.id.login_btn);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mLoginEmail.getEditText().getText().toString();
                String password = mLoginPassword.getEditText().getText().toString();

                //Check if Fields are Empty
                if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)) {

                    //Call Progress Dialog
                    mLoginProgress.setTitle("Logging In");
                    mLoginProgress.setMessage("Checking Credentials, Please Wait!");
                    mLoginProgress.setCanceledOnTouchOutside(false);
                    mLoginProgress.show();


                    login_user(email,password);
                }

                
            }
        });

    }

    private void login_user(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    mLoginProgress.dismiss();

                    String current_user_id = mAuth.getCurrentUser().getUid();
                    String deviceToken = FirebaseInstanceId.getInstance().getToken();


                    mUserDatabase.child(current_user_id).child("device_token").setValue(deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);

                            //clear previous task
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            startActivity(mainIntent);
                            finish();
                        }
                    });
                }
                else{

                    //**Improvements**

                    //Use the task from the method to display error messages

                    mLoginProgress.hide();
                    Toast.makeText(LoginActivity.this, "Cannot Sign in. Please Check and Try Again.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
