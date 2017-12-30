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


import de.hdodenhof.circleimageview.CircleImageView;

//Credits
//<div>Icons made by <a href="http://www.freepik.com" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>
//<div>Icons made by <a href="https://www.flaticon.com/authors/smashicons" title="Smashicons">Smashicons</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>


public class OrganizationActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private RecyclerView mOrgList;

    private DatabaseReference mEnrolledDatabase;
    private DatabaseReference mOrganizationsDatabase;
    private FirebaseAuth mAuth;

    private String mCurrent_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization);

        //Toolbar Set

        mToolbar = (Toolbar) findViewById(R.id.org_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Organization List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //**Previous**
        //
//        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
//        mUsersDatabase = FirebaseDatabase.getInstance().getReference()
//                .child("Organization").child(mCurrentUser.getUid());
//
//        mOrgList = (RecyclerView) findViewById(R.id.org_rec_list);
//        mOrgList.setLayoutManager(new LinearLayoutManager(this));
//        mOrgList.setHasFixedSize(true);

        //Trial
        //
        mOrgList = (RecyclerView) findViewById(R.id.org_rec_list);
        mAuth = FirebaseAuth.getInstance();

        mCurrent_user_id = mAuth.getCurrentUser().getUid();

        mEnrolledDatabase = FirebaseDatabase.getInstance().getReference().child("Enrolled").child(mCurrent_user_id);
        mOrganizationsDatabase = FirebaseDatabase.getInstance().getReference().child("Organizations");
        mEnrolledDatabase.keepSynced(true);
        mOrganizationsDatabase.keepSynced(true);

        mOrgList.setHasFixedSize(true);
        mOrgList.setLayoutManager(new LinearLayoutManager(this));//Possible error with context

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<OrganizationList, OrganizationHolder> friendsRecyclerViewAdapter
                = new FirebaseRecyclerAdapter<OrganizationList, OrganizationHolder>(

                        OrganizationList.class,
                        R.layout.activity_organization_item,
                        OrganizationHolder.class,
                        mEnrolledDatabase
        ) {
            @Override
            protected void populateViewHolder(final OrganizationHolder viewHolder, OrganizationList model, int position) {

                viewHolder.setDate(model.getDate());

                final String list_org_id = getRef(position).getKey();

                mOrganizationsDatabase.child(list_org_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final String orgName = dataSnapshot.child("name").getValue().toString();
                        final String orgThumb = dataSnapshot.child("thumb_image").getValue().toString();

                        viewHolder.setName(orgName);
                        viewHolder.setLogo(orgThumb, OrganizationActivity.this);//Possible error with context

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent classesIntent = new Intent(OrganizationActivity.this, ClassesActivity.class);
                                classesIntent.putExtra("org_id", list_org_id);
                                startActivity(classesIntent);
                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };

        mOrgList.setAdapter(friendsRecyclerViewAdapter);
    }

    public static class OrganizationHolder extends RecyclerView.ViewHolder {

        View mView;

        public OrganizationHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setDate(String date){
            TextView orgSubTitleView = (TextView) mView.findViewById(R.id.org_sub_title_lbl);
            orgSubTitleView.setText(date);
        }

        public void setName(String name){
            TextView orgTitleView = (TextView) mView.findViewById(R.id.org_title_lbl);
            orgTitleView.setText(name);
        }

        public void setLogo (final String thumb_image, final Context context){

            if (!thumb_image.equals("default")){

                final CircleImageView orgLogoImgView = (CircleImageView) mView.findViewById(R.id.org_logo_im);
                Picasso.with(context).load(thumb_image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.img).into(orgLogoImgView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(context).load(thumb_image).placeholder(R.drawable.img).into(orgLogoImgView);
                    }
                });
            }
        }
    }
}
