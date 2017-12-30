package com.example.allenrajumathew.firebasechatapp.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.allenrajumathew.firebasechatapp.OrganizationActivity;
import com.example.allenrajumathew.firebasechatapp.R;
import com.example.allenrajumathew.firebasechatapp.UI.IndividualActivity;
import com.example.allenrajumathew.firebasechatapp.UI.QuickStartRecorder;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {

    private View mMainView;

    private ImageView quickstart;
    private Button individual_btn;
    private Button organization_btn;

    Context ctx =ChatsFragment.this.getContext();

    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMainView = inflater.inflate(R.layout.fragment_chats, container, false);

        quickstart = (ImageView) mMainView.findViewById(R.id.imageView);
        individual_btn = (Button) mMainView.findViewById(R.id.individualBtn);
        organization_btn = (Button) mMainView.findViewById(R.id.organizationBtn);

        quickstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatsFragment.this.getContext(), QuickStartRecorder.class);
                startActivity(intent);
            }
        });

        individual_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatsFragment.this.getContext(), IndividualActivity.class);
                startActivity(intent);
            }
        });

        organization_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatsFragment.this.getContext(), OrganizationActivity.class);
                startActivity(intent);
            }
        });


        // Inflate the layout for this fragment
        return mMainView;
    }

}
