package com.example.allenrajumathew.firebasechatapp.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.allenrajumathew.firebasechatapp.Adapter.IndividualAdapter;
import com.example.allenrajumathew.firebasechatapp.Model.IndividualData;
import com.example.allenrajumathew.firebasechatapp.R;
import com.example.allenrajumathew.firebasechatapp.RegisterOrgActivity;
import com.example.allenrajumathew.firebasechatapp.UI.QuickStartRecorder;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestsFragment extends Fragment {

    private View mMainView;

    private ImageView addOrg;

    public RequestsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMainView = inflater.inflate(R.layout.fragment_requests, container, false);


        addOrg = (ImageView) mMainView.findViewById(R.id.request_fragment_add_org_im);


        addOrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RequestsFragment.this.getContext(), RegisterOrgActivity.class);
                startActivity(intent);
            }
        });

        // Inflate the layout for this fragment
        return mMainView;
    }


//    private View mMainView;
//
//    private RecyclerView recView;
//    private IndividualAdapter adapter;
//    private ArrayList listData;
//
//    public RequestsFragment() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        mMainView = inflater.inflate(R.layout.fragment_requests, container, false);
//
//        listData = (ArrayList) IndividualData.getListData(this.getContext());
//
//        recView = (RecyclerView) mMainView.findViewById(R.id.rec_frag_recording_list);
//
//        //****
//        //LayoutManager: LinearLayoutManager or GridLayoutManage or StaggeredGridLayoutManage
//
//        recView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
//
//        //instance
//
//        adapter = new IndividualAdapter(IndividualData.getListData(this.getContext()), this.getContext());
//
//        recView.addItemDecoration(new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL));
//
//        recView.setAdapter(adapter);
//
//
//        // Inflate the layout for this fragment
//        return mMainView;
//    }

}
